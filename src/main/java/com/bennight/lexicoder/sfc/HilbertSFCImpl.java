package com.bennight.lexicoder.sfc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.uzaygezen.core.BacktrackingQueryBuilder;
import com.google.uzaygezen.core.BitVector;
import com.google.uzaygezen.core.BitVectorFactories;
import com.google.uzaygezen.core.CompactHilbertCurve;
import com.google.uzaygezen.core.FilteredIndexRange;
import com.google.uzaygezen.core.LongContent;
import com.google.uzaygezen.core.PlainFilterCombiner;
import com.google.uzaygezen.core.Query;
import com.google.uzaygezen.core.QueryBuilder;
import com.google.uzaygezen.core.RegionInspector;
import com.google.uzaygezen.core.SimpleRegionInspector;
import com.google.uzaygezen.core.ZoomingSpaceVisitorAdapter;
import com.google.uzaygezen.core.ranges.LongRange;
import com.google.uzaygezen.core.ranges.LongRangeHome;

public class HilbertSFCImpl implements SpaceFillingCurve{

	int _bitsPerDimension = 18; 
	long _precision = (long)Math.pow(2,  _bitsPerDimension);
	CompactHilbertCurve _chc = new CompactHilbertCurve(new int[] {_bitsPerDimension, _bitsPerDimension});


	public HilbertSFCImpl(){

	}

	public HilbertSFCImpl(int bitsPerDimension){
		_bitsPerDimension = bitsPerDimension;
		_precision = (long)Math.pow(2, _bitsPerDimension);
		_chc = new CompactHilbertCurve(new int[] {_bitsPerDimension, _bitsPerDimension});
	}


	public long PointToValue(CoordinateWGS84 pt) {
		return PointToHilbert(pt);
	}

	public CoordinateWGS84 ValueToPoint(long value) {
		return HilbertToPoint(value);
	}

	public Collection<long[]> RangeQuery(CoordinateWGS84 lowerLeft, CoordinateWGS84 upperRight) {
		return rangeQuery(lowerLeft, upperRight);
	}

	private long PointToHilbert(CoordinateWGS84 point) {

		BitVector[] p = new BitVector[2];
		for (int i = p.length; --i >= 0;) {
			p[i] = BitVectorFactories.OPTIMAL.apply(_bitsPerDimension);
		}
		BitVector hilbert = BitVectorFactories.OPTIMAL.apply(_bitsPerDimension * 2);

		p[0].copyFrom(point.getNormalLongitude(_precision));
		p[1].copyFrom(point.getNormalLatitude(_precision));
		
		_chc.index(p, 0, hilbert);
		return hilbert.toLong();
	}

	private CoordinateWGS84 HilbertToPoint(long hilbertValue) {

		BitVector h = BitVectorFactories.OPTIMAL.apply(_bitsPerDimension * 2);
		h.copyFrom(hilbertValue);
		BitVector[] p = new BitVector[2];
		for (int i = 0; i < p.length; i++) {
			p[i] = BitVectorFactories.OPTIMAL.apply(_bitsPerDimension);
		}
		_chc.indexInverse(h, p);

		return new CoordinateWGS84(p[0].toLong(), p[1].toLong(), _precision);
	}


	private List<long[]>  rangeQuery(CoordinateWGS84 min, CoordinateWGS84 max){
		CompactHilbertCurve chc = new CompactHilbertCurve(new int[] {_bitsPerDimension, _bitsPerDimension});
		int maxRanges = Integer.MAX_VALUE;
		List<LongRange> region = new ArrayList<>(2);

		region.add(LongRange.of(min.getNormalLongitude(_precision), max.getNormalLongitude(_precision)));
		region.add(LongRange.of(min.getNormalLatitude(_precision), max.getNormalLatitude(_precision)));

		LongContent zero = new LongContent(0L);
		RegionInspector<LongRange,  LongContent> inspector = SimpleRegionInspector.create(ImmutableList.of(region), new LongContent(1L),  Functions.<LongRange> identity(), LongRangeHome.INSTANCE, zero);
		PlainFilterCombiner<LongRange, Long, LongContent, LongRange> combiner = new PlainFilterCombiner<LongRange, Long, LongContent, LongRange>(LongRange.of(0, 1));
		QueryBuilder<LongRange, LongRange> queryBuilder = BacktrackingQueryBuilder.create(inspector, combiner, maxRanges, true, LongRangeHome.INSTANCE, zero);
		chc.accept(new ZoomingSpaceVisitorAdapter(chc, queryBuilder));
		Query<LongRange, LongRange> query = queryBuilder.get();
		List<FilteredIndexRange<LongRange, LongRange>> ranges =  query.getFilteredIndexRanges();
		List<long[]> ranges2 = new ArrayList<long[]>();
		for (FilteredIndexRange<LongRange, LongRange> range : ranges ){
			ranges2.add(new long[] {range.getIndexRange().getStart(), range.getIndexRange().getEnd()});
		}
		return ranges2;
	}




}
