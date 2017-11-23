package com.comino.dev;

import org.ejml.data.DenseMatrix32F;

/** a 3D version of WTree, which independently tracks 3 DOFs. **/
public class WTree3
{
	DenseMatrix32F CM; /** cumulative weights, Nx3 **/

    double[] d; /** an array for offx,mulx,offy,muly,offt,mult **/

    int MAXIDX;

    public WTree3(DenseMatrix32F CM)
    {
	this.CM = CM;
	this.MAXIDX = CM.numRows;
	this.d = new double[MAXIDX*6];
    }

    public void distributeError(int a, int b, DenseMatrix32F amt)
    {
	double a0 = amt.get(0,0), a1 = amt.get(1,0), a2 = amt.get(2,0);

	double tw0 = CM.get(b, 0)-CM.get(a-1,0);
	double tw1 = CM.get(b, 1)-CM.get(a-1,1);
	double tw2 = CM.get(b, 2)-CM.get(a-1,2);

	double mul0 = a0/tw0;
	double mul1 = a1/tw1;
	double mul2 = a2/tw2;

	double off0 = CM.get(a-1, 0)*mul0;
	double off1 = CM.get(a-1, 1)*mul1;
	double off2 = CM.get(a-1, 2)*mul2;

	add(a,
	    -off0, mul0,
	    -off1, mul1,
	    -off2, mul2);

	add(b+1,
	    a0+off0, -mul0,
	    a1+off1, -mul1,
	    a2+off2, -mul2);
    }

    public void reset()
    {
	for (int i = 0; i < MAXIDX*6; i++)
	    {
		d[i] = 0;
	    }
    }

    protected void add(int idx,
		       double offx, double mulx,
		       double offy, double muly,
		       double offt, double mult)
    {
	while (idx < MAXIDX)
	    {
		int didx = idx*6;
		d[didx + 0] += offx;
		d[didx + 1] += mulx;
		d[didx + 2] += offy;
		d[didx + 3] += muly;
		d[didx + 4] += offt;
		d[didx + 5] += mult;

		if (idx == 0)
		    break;

		idx = idx + ((idx&(idx-1))^idx);
	    }
    }

    public DenseMatrix32F get(int idx)
    {
    	DenseMatrix32F p = new DenseMatrix32F(3,1);
	get(idx, p);
	return p;
    }

    public void get(int idx, DenseMatrix32F p)
    {
	float v0=0, v1=0, v2=0;
	float w0=CM.get(idx,0), w1 = CM.get(idx,1), w2 = CM.get(idx,2);
	float vv0=0, vv1=0, vv2=0;
	while (true)
	    {
		int didx = idx*6;
		v0  += d[didx + 0];
		vv0 += d[didx + 1];
		v1  += d[didx + 2];
		vv1 += d[didx + 3];
		v2  += d[didx + 4];
		vv2 += d[didx + 5];

		if (idx == 0)
		    break;

		idx = idx&(idx-1);
	    }

	v0+=vv0*w0;
	v1+=vv1*w1;
	v2+=vv2*w2;

	p.set(0,0, v0);
	p.set(1,0, v1);
	p.set(2,0, v2);
    }
}
