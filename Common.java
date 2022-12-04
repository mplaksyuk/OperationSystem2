import java.util.Random;

public class Common {

  static public int s2i (String s) {
    try {
      return Integer.parseInt(s.trim());
    } catch (NumberFormatException nfe) {
      System.out.println("NumberFormatException: " + nfe.getMessage());
    }
    return 0;
  }

  static public double R1() {
    Random generator = new Random(System.currentTimeMillis());

    double U = generator.nextDouble();
    double V = generator.nextDouble();
    double X = Math.sqrt((8 / Math.E)) * (V - 0.5) / U;

    if (V == 0 || U == 0)
      return R1();

    if (!(R2(X, U) && R3(X, U) && R4(X, U))) 
      return R1();
    
    return X;
  }
    static public boolean R2(double X, double U) {
    return (X * X) <= (5 - 4 * Math.exp(.25) * U);
  }

  static public boolean R3(double X, double U) {
    return !((X * X) >= (4 * Math.exp(-1.35) / U + 1.4));
  }

  static public boolean R4(double X, double U) {
    return (X * X) < (-4 * Math.log(U));
}

}

