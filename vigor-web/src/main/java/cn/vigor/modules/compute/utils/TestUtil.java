package cn.vigor.modules.compute.utils;
import java.util.ArrayList;
import java.util.List;

public class TestUtil {
    
    public static void main(String[] args) {
        
        //String testSql="create table temp_student select * from t_student;";
        
        
        String testSql="SELECT nhu.\"ID\",nhu.\"NUM1\",nhu.\"NUM2\",nhu.\"TIM1\",nhu.\"TIM2\",nhu.\"TIM3\" FROM lee18.nhu";
        
        List<String> tablenames = getAllTableNames(testSql.toLowerCase());
        for(String tablename:tablenames){
            System.out.println("==="+tablename);
//            System.out.println(tablename.substring(tablename.indexOf(".")+1, tablename.lastIndexOf(".")));
        }
    }
    
    public static List<String>  getAllTableNames(String sqlStr){
        String str[] = repaceWhiteSapce(sqlStr).toLowerCase().split(";");
        SqlParserUtil spu=new SqlParserUtil();
        List<String> tablenames =new ArrayList<String>();
        List<SqlSegment> result2 = null;
        for(int i=0;i<str.length;i++){
            if((str[i].contains("insert ")&&str[i].contains("select ") )  || ( str[i].contains("insert ")&&str[i].contains("partition "))){

                if(str[i].contains("join ")){
                    String s [] = str[i].split("join");
                    for(int j=0;j<s.length;j++){
                        if(j!=0){
                            s[j]=s[0]+" join "+s[j];
                        }
                        
                        result2=spu.getParsedSqlList(s[j]);//保存解析结果
                        for(SqlSegment ss:result2){
                            if(!ss.getBody().equals("")&&!ss.getBody().contains("select ")){
                            	if(ss.getStart().contains("insert ")){
                            		String s1[] = ss.getBody().split(",");
                                    for(String s2:s1){
                                        if(s2.trim().contains(" ")){
                                            if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".insert"))
                                                tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".insert");
                                        }else{
                                            if(!tablenames.contains(s2.trim() +".insert"))
                                                tablenames.add(s2.trim()+"."+"insert");
                                        }
                                    }
                            	}else{
                            		String s1[] = ss.getBody().split(",");
                                    for(String s2:s1){
                                        if(s2.trim().contains(" ")){
                                            if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".select"))
                                                tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".select");
                                        }else{
                                            if(!tablenames.contains(s2.trim() +".select"))
                                                tablenames.add(s2.trim()+"."+"select");
                                        }
                                    }

                            	}
                                
                            }else if(!ss.getBody().equals("")&&ss.getBody().contains("select ")){
                                result2=spu.getParsedSqlList(ss.getBody());//保存解析结果
                                for(SqlSegment ssss:result2){
                                    if(!ssss.getBody().equals("")&&!ssss.getBody().contains("select ")){
                                        String s1[] = ssss.getBody().split(",");
                                        for(String s2:s1){
                                            if(s2.trim().contains(" ")){
                                                if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".select"))
                                                    tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".select");
                                            }else{
                                                if(!tablenames.contains(s2.trim() +".select"))
                                                    tablenames.add(s2.trim()+"."+"select");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else{
                    result2=spu.getParsedSqlList(str[i]);//保存解析结果
                    for(SqlSegment seg:result2){
                        if(!seg.getBody().trim().equals("")&&seg.getStart().contains("insert ")){
                            if(seg.getBody().trim().contains(" ")){
                                if(!tablenames.contains(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".insert")){
                                    tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".insert");
                                }
                            }else{
                                if(!tablenames.contains(seg.getBody().trim()+".insert"))
                                    tablenames.add(seg.getBody().trim()+".insert");
                            }

                        }else if(!seg.getBody().equals("")&&(seg.getStart().contains("from ")||seg.getStart().contains("join "))){
                            if(seg.getBody().contains(" ")){
                                if(!tablenames.contains(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".select"))
                                    tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".select");
                            }else{
                                if(!tablenames.contains(seg.getBody().trim()+".select"))
                                    tablenames.add(seg.getBody().trim()+".select");
                            }
                        }
                    }
                }
            }else if(str[i].contains("insert ")){
                result2=spu.getParsedSqlList(str[i]);//保存解析结果
                for(SqlSegment seg:result2){
                    if(!seg.getBody().equals("")){
                        if(seg.getBody().trim().contains("(")){
                            if(!tablenames.contains(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf("("))+".insert"))
                                tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf("("))+".insert");
                        }else{
                            if(!tablenames.contains(seg.getBody().trim()+".insert"))
                                tablenames.add(seg.getBody().trim()+".insert");
                        }

                    }
                }
            }else if(str[i].contains("select ")&&!str[i].contains("create ")){
                if(str[i].contains("join ")){
                    String s [] = str[i].split("join");
                    for(int j=0;j<s.length;j++){
                        if(j!=0){
                            s[j]=s[0]+" join "+s[j];
                        }
                        result2=spu.getParsedSqlList(s[j]);//保存解析结果
                        for(SqlSegment ss:result2){
                            if(!ss.getBody().equals("")&&!ss.getBody().contains("select ")){
                                String s1[] = ss.getBody().split(",");
                                for(String s2:s1){
                                    if(s2.trim().contains(" ")){
                                        if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".select"))
                                            tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".select");
                                    }else{
                                        if(!tablenames.contains(s2.trim() +".select"))
                                            tablenames.add(s2.trim()+"."+"select");
                                    }
                                }

                            }else if(!ss.getBody().equals("")&&ss.getBody().contains("select ")){
                                result2=spu.getParsedSqlList(ss.getBody());//保存解析结果
                                for(SqlSegment ssss:result2){
                                    if(!ssss.getBody().equals("")&&!ssss.getBody().contains("select ")){
                                        String s1[] = ssss.getBody().split(",");
                                        for(String s2:s1){
                                            if(s2.trim().contains(" ")){
                                                if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".select"))
                                                    tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".select");
                                            }else{
                                                if(!tablenames.contains(s2.trim() +".select"))
                                                    tablenames.add(s2.trim()+"."+"select");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }else if(str[i].contains("union ")){
                    String s [] = str[i].split("union");
                    for(String sss :s){
                        result2=spu.getParsedSqlList(sss);//保存解析结果
                        for(SqlSegment ss:result2){
                            if(!ss.getBody().equals("")&&!ss.getBody().contains("select")){
                                String s1[] = ss.getBody().split(",");
                                for(String s2:s1){
                                    if(s2.trim().contains(" ")){
                                        if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".select"))
                                            tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".select");
                                    }else{
                                        if(!tablenames.contains(s2.trim() +".select"))
                                            tablenames.add(s2.trim()+"."+"select");
                                    }
                                }
                            }else if(!ss.getBody().equals("")&&ss.getBody().contains("select ")){
                                result2=spu.getParsedSqlList(ss.getBody());//保存解析结果
                                for(SqlSegment ssss:result2){
                                    if(!ssss.getBody().equals("")&&!ssss.getBody().contains("select ")){
                                        String s1[] = ssss.getBody().split(",");
                                        for(String s2:s1){
                                            if(s2.trim().contains(" ")){
                                                if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".select"))
                                                    tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".select");
                                            }else{
                                                if(!tablenames.contains(s2.trim() +".select"))
                                                    tablenames.add(s2.trim()+"."+"select");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }else if(str[i].contains("from ")&&str[i].split("from").length>2){
                    String s [] = str[i].split("from");
                    for(int j=0;j<s.length;j++){
                        if(j!=0){
                            s[j]=s[0]+" from "+s[j];
                        }
                        result2=spu.getParsedSqlList(s[j]);//保存解析结果
                        for(SqlSegment ss:result2){
                            if(!ss.getBody().equals("")){
                                String s1[] = ss.getBody().split(",");
                                for(String s2:s1){
                                    if(s2.trim().contains(" ")){
                                        if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".select"))
                                            tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".select");
                                    }else{
                                        if(!tablenames.contains(s2.trim() +".select"))
                                            tablenames.add(s2.trim()+"."+"select");
                                    }
                                }

                            }
                        }
                    }
                }else{
                    result2=spu.getParsedSqlList(str[i]);//保存解析结果
                    for(SqlSegment ss:result2){
                        if(!ss.getBody().equals("")){
                            String s1[] = ss.getBody().split(",");
                            for(String s2:s1){
                                if(s2.trim().contains(" ")){  
                                    if(!tablenames.contains(s2.trim().substring(0,s2.trim().indexOf(" "))+".select"))
                                        tablenames.add(s2.trim().substring(0,s2.trim().indexOf(" "))+".select");
                                }else{
                                    if(!tablenames.contains(s2.trim() +".select"))
                                        tablenames.add(s2.trim()+"."+"select");
                                }
                            }

                        }
                    }
                }
            }else if(str[i].contains("create ")){
                if(str[i].contains("select ")){
                    String[] sql =str[i].split("select");
                    for(int j=0;j<sql.length;j++){
                        if(j!=0){
                            sql[j]="select "+sql[j];
                        }

                        if(sql[j].contains("create ")){
                            result2=spu.getParsedSqlList(sql[j]);//保存解析结果
                            for(SqlSegment seg:result2){
                                if(!seg.getBody().equals("")){

                                    if(seg.getBody().trim().contains(" ")){
                                        if(!tablenames.contains(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".create"))
                                            tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+"."+"create");
                                    }else{
                                        if(!tablenames.contains(seg.getBody().trim()+".create"))
                                            tablenames.add(seg.getBody().trim()+"."+"create");
                                    }
                                }
                            }
                        }else if(sql[j].contains("join ")){
                            String sl[] = sql[j].split("join");
                            for(int t=0;t<sl.length;t++){
                                if(t!=0){
                                    sl[t]=sl[0]+" join "+sl[t];
                                }
                                result2=spu.getParsedSqlList(sl[t]);//保存解析结果
                                for(SqlSegment seg:result2){
                                    if(!seg.getBody().equals("")){

                                        if(seg.getBody().trim().contains(" ")){
                                            if(!tablenames.contains(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".select"))
                                                tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+"."+"select");
                                        }else{
                                            if(!tablenames.contains(seg.getBody().trim()+".select"))
                                                tablenames.add(seg.getBody().trim()+"."+"select");
                                        }
                                    }
                                }
                            }

                        }else{
                            result2=spu.getParsedSqlList(sql[j]);//保存解析结果
                            for(SqlSegment seg:result2){
                                if(!seg.getBody().equals("")){
                                    if(seg.getBody().trim().contains(" ")){
                                        if(!tablenames.contains(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+"."+"select"))
                                            tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+"."+"select");
                                    }else{
                                        if(!tablenames.contains(seg.getBody().trim()+"."+"select"))
                                            tablenames.add(seg.getBody().trim()+"."+"select");
                                    }
                                }
                            }
                        }
                    }

                }else{
                    result2=spu.getParsedSqlList(str[i]);//保存解析结果
                    for(SqlSegment seg:result2){
                        if(!seg.getBody().equals("")){
                            if(seg.getBody().trim().contains(" ")){
                                if(!tablenames.contains(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+"."+"create"))
                                    tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+"."+"create");
                            }else{
                                if(!tablenames.contains(seg.getBody().trim()+"."+"create"))
                                    tablenames.add(seg.getBody().trim()+"."+"create");
                            }
                        }
                    }
                }

            }else if(str[i].contains("alter ")){
                result2=spu.getParsedSqlList(str[i]);//保存解析结果
                for(SqlSegment seg:result2){
                    if(!seg.getBody().trim().equals("")){
                        if(seg.getBody().trim().contains(" ")){
                            tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".alter");
                        }else{
                            tablenames.add(seg.getBody().trim()+".alter");
                        }

                    }
                }
            }else if(str[i].contains("delete ")){
                result2=spu.getParsedSqlList(str[i]);//保存解析结果
                for(SqlSegment seg:result2){
                    if(!seg.getBody().equals("")){
                        if(seg.getBody().trim().contains(" ")){
                            tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".delete");
                        }else{
                            tablenames.add(seg.getBody().trim()+".delete");
                        }
                    }
                }
            }else if(str[i].contains("update ")){
                result2=spu.getParsedSqlList(str[i]);//保存解析结果
                for(SqlSegment seg:result2){
                    if(!seg.getBody().equals("")){
                        if(seg.getBody().trim().contains(" ")){
                            tablenames.add(seg.getBody().trim().substring(0,seg.getBody().trim().indexOf(" "))+".update");
                        }else{
                            tablenames.add(seg.getBody().trim()+".update");
                        }
                    }
                }
            }
        }
        return tablenames;
    }



    public static String repaceWhiteSapce(String original){
        StringBuilder sb = new StringBuilder();
        boolean isFirstSpace = false;//标记是否是第一个空格

        //        original = original.trim();//如果考虑开头和结尾有空格的情形

        char c;
        for(int i = 0; i < original.length(); i++){
            c = original.charAt(i);
            if(c == ' ' || c == '\t')//遇到空格字符时,先判断是不是第一个空格字符
            {
                if(!isFirstSpace)
                {
                    sb.append(c);
                    isFirstSpace = true;
                }
            }else if(c == '\n'){
            	if(original.charAt(i-1)!=' ' && original.charAt(i-1)!='\t'){
            		sb.append(" ");
            	}
            }
            else{//遇到非空格字符时
                sb.append(c);
                isFirstSpace = false;
            }
        }
        return sb.toString();
    }

    
}