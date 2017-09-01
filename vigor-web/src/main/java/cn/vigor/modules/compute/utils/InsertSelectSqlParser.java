package cn.vigor.modules.compute.utils;

public class InsertSelectSqlParser extends BaseSingleSqlParser{
	public InsertSelectSqlParser(String originalSql) {
		super(originalSql);
	}
	@Override
	protected void initializeSegments() {
		segments.add(new SqlSegment("(insert into)(.+)( as | select )","[,]"));
		segments.add(new SqlSegment("(insert overwrite table )(.+)( select )","[,]"));
		segments.add(new SqlSegment("(insert overwrite table )(.+)( partition)([(])","[,]"));
//		segments.add(new SqlSegment("(select)(.+)(from)","[,]"));
//		segments.add(new SqlSegment("(from)(.+)( where | on | having | groups+by | orders+by | ENDOFSQL)","(,|s+lefts+joins+|s+rights+joins+|s+inners+joins+)"));
		segments.add(new SqlSegment("(from)(.+)( where | on | having | groups+by | orders+by | join | ENDOFSQL)","[,]"));
		segments.add(new SqlSegment("(join)(.+)(on)","[,]"));
//		segments.add(new SqlSegment("( where | on | having )(.+)( groups+by | orders+by | ENDOFSQL)","(and|or)"));
//		segments.add(new SqlSegment("(groups+by)(.+)( orders+by| ENDOFSQL)","[,]"));
//		segments.add(new SqlSegment("(orders+by)(.+)( ENDOFSQL)","[,]"));
	}
}