package com.bennight.lexicoder;

import java.util.Collection;
import org.apache.accumulo.core.client.lexicoder.Lexicoder;
import org.apache.accumulo.core.client.lexicoder.LongLexicoder;
import org.apache.accumulo.core.iterators.ValueFormatException;

import com.bennight.lexicoder.sfc.CoordinateWGS84;
import com.bennight.lexicoder.sfc.HilbertSFCImpl;
import com.bennight.lexicoder.sfc.SpaceFillingCurve;

public class LexicoderWGS84 implements Lexicoder<CoordinateWGS84> {

	private LongLexicoder _ll = new LongLexicoder();
	private SpaceFillingCurve _sfc = new HilbertSFCImpl();

	public void SetBits(int bits){
		_sfc = new HilbertSFCImpl(bits);
	}
	
	public byte[] encode(CoordinateWGS84 v) {
		long sfcid = _sfc.PointToValue(v);
		return _ll.encode(sfcid);
	}

	public CoordinateWGS84 decode(byte[] b) throws ValueFormatException {
		long hid = _ll.decode(b);
		return _sfc.ValueToPoint(hid);
	}

	public Collection<long[]> QueryWindow(CoordinateWGS84 lowerLeft, CoordinateWGS84 upperRight){

		return _sfc.RangeQuery(lowerLeft, upperRight);
	}


	public static void main(String[] args){
		

		long time = 0;
		long t2 = 0;
		System.out.println("----------------------------------------------------");
		for (int i = 2; i <= 24; i++){
			HilbertSFCImpl chc = new HilbertSFCImpl(i);
			time = System.currentTimeMillis();
			Collection<long[]> r = chc.RangeQuery(new CoordinateWGS84(-178.123456, -86.398493), new CoordinateWGS84(179.3211113, 87.393483));
			t2 = System.currentTimeMillis() - time;
			System.out.println(String.format("%d bits took %.3f seconds for  %d ranges", i, t2 / 1000f, r.size()));
		
			LexicoderWGS84 lxc = new LexicoderWGS84();
			lxc.SetBits(i);
			double sumActualErrorLat = 0;
			double sumAbsErrorLon = 0;
			int numIterations = 0;
			for (int x = -180; x <= 180; x+=2.2345678){
				for (int y = -90; y <= 90; y+=1.84848483 ){
					CoordinateWGS84 initial = new CoordinateWGS84((double)x,(double)y);
					initial = lxc.decode(lxc.encode(initial));
					sumAbsErrorLon += Math.abs(x - initial.getLongitude());
					sumActualErrorLat += (y - initial.getLatitude());
					numIterations++;
				}
			}
			System.out.println(String.format("Actual lattitude error:         %.7f", sumActualErrorLat / numIterations));
			System.out.println(String.format("Worst case avg longitude error: %.7f", sumAbsErrorLon / numIterations));
			System.out.println("----------------------------------------------------");

		}

	}

}
