package cn.vigor.modules.compute.utils;

public class AlterSqlParser extends BaseSingleSqlParser{
public AlterSqlParser(String originalSql) {
    super(originalSql);
}
@Override
protected void initializeSegments() {
    segments.add(new SqlSegment("(alter table)(.+)( add | drop | rename | replace  )","[,]"));
}
@Override
public String getParsedSql() {
    String retval=super.getParsedSql();
    retval=retval+")";
    return retval;
}
}