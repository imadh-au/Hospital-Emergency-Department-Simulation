package project;


public class StatisticalUtils {
	 // Weibull distribution generator
	public static double generateWeibull(double x, double alpha, double beta) {
        if (x < 0) return 0;
        double part1 = (alpha / beta);
        double part2 = Math.pow(x / beta, alpha - 1);
        double part3 = Math.exp(-Math.pow(x / beta, alpha));
        return part1 * part2 * part3;
    }

    // Exponential distribution generator
	public static double generatePearsonVI(double x, double beta, double p, double q) {
        if (x < 0) return 0;
        double betaFunc = betaFunction(p, q);
        double numerator = Math.pow(x / beta, p - 1);
        double denominator = beta * betaFunc * Math.pow(1 + (x / beta), p + q);
        return numerator / denominator;
    }

    // Exponential Distribution
    public static double generateExponential(double x, double mu) {
        if (x < 0) return 0;
        return (1 / mu) * Math.exp(-x / mu);
    }

    
    public static double betaFunction(double p, double q) {
        return gammaFunction(p) * gammaFunction(q) / gammaFunction(p + q);
    }
    
    public static double gammaFunction(double z) {
        double[] p = {
                676.5203681218851,
                -1259.1392167224028,
                771.32342877765313,
                -176.61502916214059,
                12.507343278686905,
                -0.13857109526572012,
                9.9843695780195716e-6,
                1.5056327351493116e-7
        };

        double g = 7;
        if (z < 0.5) {
            return Math.PI / (Math.sin(Math.PI * z) * gammaFunction(1 - z));
        }

        z -= 1;
        double x = 0.99999999999980993;
        for (int i = 0; i < p.length; i++) {
            x += p[i] / (z + i + 1);
        }

        double t = z + g + 0.5;
        return Math.sqrt(2 * Math.PI) * Math.pow(t, z + 0.5) * Math.exp(-t) * x;
    }
}
