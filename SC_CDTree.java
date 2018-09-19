
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;

class SC_Tree
{
    static final int MAXN=100_005;
    static ArrayList<Integer>[] graph,cenTree;
    static Edge[] edges;
    static int[] depth,parent,chCount;
    static int n,q,root;
    
    static IO io=new IO(System.in);
    
    public static void main(String[] args)throws Exception
    {
        int i;
        for(int tc=io.nextInt();tc>0;--tc)
        {
            n=io.nextInt();     q=io.nextInt();
            graph = new ArrayList[n];
            edges = new Edge[n-1];
            
            for(i=0;i<graph.length;++i)graph[i]=new ArrayList<>();
            
            for(i=0;i<n-1;++i)
            {
                int a=io.nextInt()-1,b=io.nextInt()-1;
                edges[i] = new Edge(a,b,io.nextLong());
                graph[a].add(i);    graph[b].add(i);
            }
            
            cenTree=new ArrayList[n];   removedCh=new int[n];
            for(i=0;i<cenTree.length;++i)cenTree[i]=new ArrayList<>();
            
            root=0; organise(true);
            root = constructTree(0,new HashSet<>());
            organise(true);
            
            for(;q>0;--q)
            {
                
            }
        }
        io.flush();
    }
    
    static int[] queue=new int[MAXN],removedCh;
    
    static int constructTree(int r,HashSet<Integer> added)
    {
        int cen = findCentroid(r,added);
        added.add(cen);
        
        Iterator<Integer> itr = graph[cen].iterator();
        while(itr.hasNext())
        {
            int ch = edges[itr.next()].getOtherVertex(cen);
            if(ch == parent[cen] || added.contains(ch))continue;
            int v = constructTree(ch,added);
            cenTree[cen].add(v);    cenTree[v].add(cen);
        }
        int copy=cen;
        for(;parent[copy]>=0 && copy!=r;)
        {
            copy=parent[copy];
            removedCh[copy] += chCount[cen] - removedCh[cen];
            if(copy == r)
            {
                int v=constructTree(r,added);
                cenTree[cen].add(v);    cenTree[v].add(cen);
                break;
            }
        }
        return cen;
    }
    static int findCentroid(int root,HashSet<Integer> added)
    {
        int nodeNum = chCount[root] - removedCh[root];
        if(nodeNum <= 2)return root;
        int target = nodeNum/2;
        boolean again=true;
        while(again)
        {
            again=false;
            Iterator<Integer> itr = graph[root].iterator();
            while(itr.hasNext())
            {
                int ch = edges[itr.next()].getOtherVertex(root);
                if(ch==parent[root] || added.contains(ch))continue;
                if(chCount[ch]-removedCh[ch] > target)
                {
                    root=ch;    again=true; break;
                }
            }
        }
        return root;
    }
    static void organise(boolean weighted)  // true if edges[] is to be accessed
    {
        parent=new int[n];  depth=new int[n];
        chCount=new int[n]; Arrays.fill(chCount, 1);
        int i,st=0,end=0;
        parent[root]=-1;    depth[root]=1;
        queue[end++]=root;
        while(st<end)
        {
            int node = queue[st++], h = depth[node]+1;
            Iterator<Integer> itr = graph[node].iterator();
            while(itr.hasNext())
            {
                int ch = weighted? edges[itr.next()].getOtherVertex(node): itr.next();
                if(depth[ch]>0)continue;
                depth[ch]=h;   parent[ch]=node;
                queue[end++]=ch;
            }
        }
        for(i=n-1; i>=0; --i)
            if(parent[queue[i]]>=0)
                chCount[parent[queue[i]]] += chCount[queue[i]];
    }
    
    static void navigate(int u,int v)
    {
        while(depth[u] < depth[v])
        {
            v=parent[v];
        }
        while(depth[u] > depth[v])
        {
            u=parent[u];
        }
        while(u!=v)
        {
            u=parent[u];        v=parent[v];
        }
    }
}
class Edge
{
    int u,v;    long wt;
    Edge(int a,int b,long val){u=a;v=b;wt=val;}
    int getOtherVertex(int a){return a==u? v: a==v? u: -1;}
}
class IO
{
    static byte[] buf = new byte[2048];
    static int index, total;
    static InputStream in;
    static StringBuilder sb = new StringBuilder();
    
 
    IO(InputStream is)
    {
        in = is;
    }
 
    int scan() throws Exception
    {
        if(index>=total){
            index = 0;
            total = in.read(buf);
            if(total<=0)
                return -1;
        }
        return buf[index++];
    }
 
    String next() throws Exception
    {
        int c;
        for(c=scan(); c<=32; c=scan());
        StringBuilder sb = new StringBuilder();
        for(; c>32; c=scan())
            sb.append((char)c);
        return sb.toString();
    }
 
    int nextInt() throws Exception
    {
        int c, val = 0;
        for(c=scan(); c<=32; c=scan());
        boolean neg = c=='-';
        if(c=='-' || c=='+')
            c = scan();
        for(; c>='0' && c<='9'; c=scan())
            val = (val<<3) + (val<<1) + (c&15);
        return neg?-val:val;
    }
    long nextLong() throws Exception
    {
        int c;long val = 0;
        for(c=scan(); c<=32; c=scan());
        boolean neg = c=='-';
        if(c=='-' || c=='+')
            c = scan();
        for(; c>='0' && c<='9'; c=scan())
            val = (val<<3) + (val<<1) + (c&15);
        return neg?-val:val;
    }
    void print(Object a)
    {
        sb.append(a.toString());
    }
    void println(Object a)
    {
        sb.append(a.toString()).append("\n");
    }
    void println()
    {
        sb.append("\n");
    }
    void flush()
    {
        System.out.print(sb);
        sb = new StringBuilder();
    }
}