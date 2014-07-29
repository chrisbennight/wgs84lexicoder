package com.bennight.lexicoder.sfc;

import java.util.Collection;



public interface SpaceFillingCurve {

	public long PointToValue(CoordinateWGS84 pt);

	public CoordinateWGS84 ValueToPoint(long value);

	public Collection<long[]> RangeQuery(CoordinateWGS84 lowerLeft, CoordinateWGS84 upperRight);


}
