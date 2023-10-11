package app.common.svg;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

public class Arc2Curve {
    private static final double TAU = Math.PI * 2;

    private static double unitVectorAngle(double ux, double uy, double vx, double vy) {
        double sign = (ux * vy - uy * vx < 0) ? -1 : 1;
        double dot = ux * vx + uy * vy;

        if (dot > 1.0) {
            dot = 1.0;
        }
        if (dot < -1.0) {
            dot = -1.0;
        }

        return sign * Math.acos(dot);
    }

    private static double[] getArcCenter(double x1, double y1, double x2, double y2, int fa, int fs, double rx, double ry, double sinPhi, double cosPhi) {
        double x1p = cosPhi * (x1 - x2) / 2 + sinPhi * (y1 - y2) / 2;
        double y1p = -sinPhi * (x1 - x2) / 2 + cosPhi * (y1 - y2) / 2;

        double rxSq = rx * rx;
        double rySq = ry * ry;
        double x1pSq = x1p * x1p;
        double y1pSq = y1p * y1p;

        double radicant = (rxSq * rySq) - (rxSq * y1pSq) - (rySq * x1pSq);

        if (radicant < 0) {
            radicant = 0;
        }

        radicant /= (rxSq * y1pSq) + (rySq * x1pSq);
        radicant = Math.sqrt(radicant) * (fa == fs ? -1 : 1);

        double cxp = radicant * rx / ry * y1p;
        double cyp = radicant * -ry / rx * x1p;

        double cx = cosPhi * cxp - sinPhi * cyp + (x1 + x2) / 2;
        double cy = sinPhi * cxp + cosPhi * cyp + (y1 + y2) / 2;

        double v1x = (x1p - cxp) / rx;
        double v1y = (y1p - cyp) / ry;
        double v2x = (-x1p - cxp) / rx;
        double v2y = (-y1p - cyp) / ry;

        double theta1 = unitVectorAngle(1, 0, v1x, v1y);
        double deltaTheta = unitVectorAngle(v1x, v1y, v2x, v2y);

        if (fs == 0 && deltaTheta > 0) {
            deltaTheta -= TAU;
        }
        if (fs == 1 && deltaTheta < 0) {
            deltaTheta += TAU;
        }

        return new double[]{cx, cy, theta1, deltaTheta};
    }

    private static double[] approximateUnitArc(double theta1, double deltaTheta) {
        double alpha = 4.0 / 3.0 * Math.tan(deltaTheta / 4);

        double x1 = Math.cos(theta1);
        double y1 = Math.sin(theta1);
        double x2 = Math.cos(theta1 + deltaTheta);
        double y2 = Math.sin(theta1 + deltaTheta);

        return new double[]{x1, y1, x1 - y1 * alpha, y1 + x1 * alpha, x2 + y2 * alpha, y2 - x2 * alpha, x2, y2};
    }

    public static List<double[]> a2c(double x1, double y1, double x2, double y2, int fa, int fs, double rx, double ry, double phi) {
        double sinPhi = Math.sin(Math.toRadians(phi));
        double cosPhi = Math.cos(Math.toRadians(phi));

        double x1p = cosPhi * (x1 - x2) / 2 + sinPhi * (y1 - y2) / 2;
        double y1p = -sinPhi * (x1 - x2) / 2 + cosPhi * (y1 - y2) / 2;

        if (x1p == 0 && y1p == 0) {
            return new ArrayList<>();
        }

        if (rx == 0 || ry == 0) {
            return new ArrayList<>();
        }

        rx = Math.abs(rx);
        ry = Math.abs(ry);

        double lambda = (x1p * x1p) / (rx * rx) + (y1p * y1p) / (ry * ry);
        if (lambda > 1) {
            rx *= Math.sqrt(lambda);
            ry *= Math.sqrt(lambda);
        }

        double[] cc = getArcCenter(x1, y1, x2, y2, fa, fs, rx, ry, sinPhi, cosPhi);

        List<double[]> result = new ArrayList<>();
        double theta1 = cc[2];
        double deltaTheta = cc[3];

        int segments = (int) Math.ceil(Math.abs(deltaTheta) / (TAU / 4)); // MISSING MAX
        deltaTheta /= segments;

        for (int i = 0; i < segments; i++) {
            result.add(approximateUnitArc(theta1, deltaTheta));
            theta1 += deltaTheta;
        }

        for (double[] curve : result) {
            for (int i = 0; i < curve.length; i += 2) {
                double x = curve[i];
                double y = curve[i + 1];

                x *= rx;
                y *= ry;

                double xp = cosPhi * x - sinPhi * y;
                double yp = sinPhi * x + cosPhi * y;

                curve[i] = xp + cc[0];
                curve[i + 1] = yp + cc[1];
            }
        }

        return result;
    }
}
