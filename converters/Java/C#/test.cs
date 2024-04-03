public sealed class Test
{
    public static void Main(string[] args)
    {
        foreach (string s in args)
        {
            if (s.Length > 2)
            {
                Console.WriteLine(s.Substring(2));
                return;
            } 
            
            Console.WriteLine("To short");
        }
    }

    public void a(out bool b)
    {
        
    }
}