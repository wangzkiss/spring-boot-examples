package cn.vigor.modules.compute.utils;

public class SelectSqlParser extends BaseSingleSqlParser{
	public SelectSqlParser(String originalSql) {
		super(originalSql);
	}
@Override
	protected void initializeSegments() {
//    	segments.add(new SqlSegment("(select)(.+)(from)","[,]"));
      	segments.add(new SqlSegment("(from)(.+)([)] | JOIN | where | on | having | group by | order by | ENDOFSQL)","[,]"));
      	segments.add(new SqlSegment("(join)(.+)( on )","[,]"));
//    	segments.add(new SqlSegment("(where|on|having)(.+)( group by | order by | ENDOFSQL)","(and|or)"));
//    	segments.add(new SqlSegment("(group by)(.+)( order by| ENDOFSQL)","[,]"));
//    	segments.add(new SqlSegment("(order by)(.+)( ENDOFSQL)","[,]"));
	}
}

