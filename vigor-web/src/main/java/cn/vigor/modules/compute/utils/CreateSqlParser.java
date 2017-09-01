package cn.vigor.modules.compute.utils;

public class CreateSqlParser extends BaseSingleSqlParser{
public CreateSqlParser(String originalSql) {
    super(originalSql);
}
@Override
protected void initializeSegments() {
    segments.add(new SqlSegment("(create table)(.+)( as | select | like |[(])","[,]"));
    segments.add(new SqlSegment("(create external table)(.+)( like |[(])","[,]"));
   
}
}