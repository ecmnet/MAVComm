package com.comino.dev;

/**
 * This class allows the user to distribute error into a range of
 * consecutive buckets, with each bucket having a certain "weight",
 * such that the error that is distributed proportionally to the
 * weight of the buckets.
 *
 * For example:
 * index  weight  (weightsum)  offset  mul   value
 *  0       1         1          0      0      0
 *  1       2         3          0      0      0
 *  2       2         5          0      0      0
 *  3       1         6          0      0      0
 *  4       3         9          0      0      0
 *  5       1         10         0      0      0
 *  6       2         12         0      0      0
 *  7       3         15         0      0      0
 *  8       1         16         0      0      0
 *  9       2         18         0      0      0
 *
 * Suppose you have 18 units of error that you wish to distribute
 * amongst buckets 4-7. a = 4, b = 7, s = 18.
 *
 * index  weight  (weightsum)  offset  mul   value = offset + weightsum*mul
 *  0       1         1          0      0      0
 *  1       2         3          0      0      0
 *  2       2         5          0      0      0
 *  3       1         6          0      0      0
 *  4       3         9          -12    2      6  = -12 + 2*9
 *  5       1         10         -12    2      8  = -12 + 2*10
 *  6       2         12         -12    2      12 = -12 + 2*12
 *  7       3         15         -12    2      18 = -12 + 2*15
 *  8       1         16         18     0      18
 *  9       2         18         18     0      18
 *
 * By selecting appropriate values of offset and mul for each element,
 * we can generate the correct values.
 *
 * Where did the values in offset and mul come from?
 *
 *  2 = s/(weightsum[b]-weightsum[a-1])
 *  -12 = weightsum[a-1] * mul
 *  18 = s
 *
 * Note that all of the operations we performed are linear, thus if we
 * wanted to distribute -6 units of error between [1,6], we simply
 * element-wise add the arrays offset and mul.
 *
 * The critical feature of this representation is that the values
 * offset and mul are constant over all the elements in the range
 * [a,b]. We can perform updates like this by using a tree in log(N)
 * time.
 *
 * The tree data structure actually implements only one operations:
 * add(value, firstidx), which adds "value" to *all* indices greater
 * than or equal to firstidx. To add to only a range of indices, we
 * add(value, firstidx) and add(-value, lastidx+1).
 *
 * The tree is packed into a 1 dimensional array for efficiency. The
 * value of a leaf is the sum at all the nodes on the path from the
 * root to the leaf. The actual indexing of the tree is somewhat icky.
 *
 *                                         0
 *                                        / \
 *                                       /   \
 *                                      /     \
 *                                     /       \
 *                                    /         \
 *                                   /           \
 *                                  /             \
 *                                 0               4
 *                                / \             / \
 *                               /   \           /   \
 *                              /     \         /     \
 *                             0       2       4       6
 *                            / \     / \     / \     / \
 *                           0   1   2   3   4   5   6   7
 *
 * Invariant: The "address" of each node is the same as the smallest
 * leaf beneath it.  To compute the value of a leaf, add all of the
 * nodes from the root to the leaf (but only add any particular
 * address once.)
 *
 * To add a value to all elements >= some index, add that value at the
 * index, and to the top node of any other disjoint subtrees. This
 * ensures that the path from the root to the other nodes contains
 * that increment exactly once.
 *
 * index        get set             add set (qty value is added to each)
 *
 *  0           0                   0
 *  1           1 + 0               1, 2, 4
 *  2           2 + 0               2, 4
 *  3           3 + 2 + 0           3, 4
 *  4           4 + 0               4
 *  5           5 + 4 + 0           5, 6
 *  6           6 + 4 + 0           6
 *  7           7 + 6 + 4 + 0       7
 *
 * Algorithmically, get() and add() can be performed with a sequence
 * of "clever" bit operations.  Note that get() repeatedly clears the
 * lowest bit of the idx (e.g., 7 = 7 + 6 + 4 + 0). Add repeatedly
 * adds the smallest factor which is a power of two.
 *
 * get(idx):
 *   value = 0;
 *   while (true) {
 *     value += r[idx];
 *     if (idx == 0)
 *         break;
 *     idx = idx&(idx-1);    // This clears the lowest bit of idx.
 *   }
 *
 * add(value, idx):
 *   while (idx < MAXINDEX) {
 *     r[idx] += value;
 *     if (idx==0)
 *        break;
 *     idx = idx + ((idx&(idx-1))^idx); // add the lowest factor which is power of two
 *   }
 *
 *
 **/
public class WTree
{
    double[] weightsum;
    double[] d; // an array for both off and mul (interleaved)
    int MAXIDX;

    /** Create a new WTree, using the provided *cumulative*
     * weights. The cumulative sum array must be positive and
     * monotonically increasing.
     **/
    public WTree(double[] weightsum)
    {
	this.weightsum = weightsum;
	MAXIDX = weightsum.length;
	d = new double[MAXIDX*2];
    }

    /** Distribute "amt" error between buckets [a,b], using previously
     * specified weights. **/
    public void distributeError(int a, int b, double amt)
    {
	double totalweight = weightsum[b]-weightsum[a-1];
	double mul = amt / totalweight;
	double off = weightsum[a-1] * mul;

	add(a, -off, mul);
	add(b+1, amt + off, -mul);
    }

    /** Get the current value of the specified index. Runs in O(log N) time. **/
    public double get(int idx)
    {
	double value = 0;
	double w = weightsum[idx];

	while (true)
	    {
		int didx = idx*2;
		value += d[didx + 0];
		value += d[didx + 1] * w;

		if (idx == 0)
		    break;

		idx = idx&(idx-1);
	    }

	return value;
    }

    /** Inserts increments into the tree. (There are actually two
	trees, one for off and mul, but we always access both at the
	same time, so we do the operations on both simultaneously. The
	array d[] contains both trees, interleaved.
    **/
    protected void add(int idx, double off, double mul)
    {
	while (idx < MAXIDX)
	    {
		int didx = idx*2;
		d[didx + 0] += off;
		d[didx + 1] += mul;

		if (idx == 0)
		    break;

		idx = idx + ((idx&(idx-1))^idx);
	    }
    }

    /** This is a simple test harness that performs the test given in
     * the documentation above.
     **/
    public static void main(String args[])
    {
	// weighting specified in the example problem above.
	//	WTree wt = new WTree(new double[] { 1, 3, 5, 6, 9, 10, 12, 15, 16, 18});

	// uniform weighting.
	//	WTree wt = new WTree(new double[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 });

	// lumpy weighting.
	WTree wt = new WTree(new double[] { 1, 2, 3, 4, 5, 100, 101, 102, 103, 104 });

	wt.distributeError(4, 7, 18);
	//	wt.distributeError(1, 6, -6);

	for (int i = 0; i <= 9; i++)
	    {
		System.out.println(String.format("%3d: %8.3f", i, wt.get(i)));
	    }
    }
}
