package dev.progames723.hmmm.utils;

import dev.progames723.hmmm.ActualSecureRandom;
import dev.progames723.hmmm.HmmmLibrary;
import net.minecraft.Util;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.Random;

@SuppressWarnings("unused")
public class MathUtil {
    private static boolean works = false;
    private static boolean initialized = false;
    
    private static final SecureRandom actualSecureRandom = ActualSecureRandom.createSecureRandom();
    
    private static native void registerNatives();
    
    /**
     * damn
     */
    public static void loadLibrary() {
        if (!initialized) {
            InputStream linuxLibraryX64 = Thread.currentThread().getContextClassLoader().getResourceAsStream("native_libs/mathUtil/linux/x64/libmathUtil.so");
            InputStream windowsLibraryX86 = Thread.currentThread().getContextClassLoader().getResourceAsStream("native_libs/mathUtil/windows/x86/mathUtil.dll");
            InputStream windowsLibraryX64 = Thread.currentThread().getContextClassLoader().getResourceAsStream("native_libs/mathUtil/windows/x64/mathUtil.dll");
            
            assert linuxLibraryX64 != null; assert windowsLibraryX64 != null; assert windowsLibraryX86 != null; //the great assert wall
            
            switch (Util.getPlatform()) {
                case LINUX -> {
                    try {
                        NativeUtil.loadLibrary(linuxLibraryX64, "libmathUtil.so");
//					    System.load(linuxLibraryX64.getAbsolutePath());
                        PlatformUtil.initArchitecture(PlatformUtil.Architecture.X64);
                    } catch (UnsatisfiedLinkError e) {
                        PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
                        HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Unsupported Linux CPU Architecture!", e);
                        works = false;
                    } catch (IOException e) {
                        works = false;
                        HmmmLibrary.LOGGER.error("Encountered an IO exception", e);
                        PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
                    }
                }
                case WINDOWS -> {
                    try {
                        NativeUtil.loadLibrary(windowsLibraryX64, "mathUtil.dll");
//					    System.load("E:\\IdeaProjects\\hmmm library\\nativeLibSrc\\build\\native_libs\\mathUtil\\windows\\x64\\mathUtil.dll");
                        PlatformUtil.initArchitecture(PlatformUtil.Architecture.X64);
                    } catch (UnsatisfiedLinkError e) {
                        try {
                            NativeUtil.loadLibrary(windowsLibraryX86, "mathUtil.dll");
//							System.load(windowsLibraryX86.getAbsolutePath());
                            PlatformUtil.initArchitecture(PlatformUtil.Architecture.X86);
                        } catch (UnsatisfiedLinkError e1) {
                            HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Unsupported Windows CPU Architecture!", e1);
                            PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
                            works = false;
                        } catch (IOException e1) {
                            works = false;
                            HmmmLibrary.LOGGER.error("Encountered an IO exception", e1);
                            PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
                        }
                    } catch (IOException e) {
                        works = false;
                        HmmmLibrary.LOGGER.error("Encountered an IO exception", e);
                        PlatformUtil.initArchitecture(PlatformUtil.Architecture.getArchitecture());
                    }
                }
                default -> {
                    HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Unsupported OS!");
                    works = false;
                }
            }
            try {
                linuxLibraryX64.close();
                windowsLibraryX64.close();
                windowsLibraryX86.close();
            } catch (IOException e) {
                HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Cannot close some file's stream!", e);
            }
            if (works) {
                try {
                    registerNatives();
                } catch (UnsatisfiedLinkError e) {
                    HmmmLibrary.LOGGER.error(HmmmLibrary.NATIVE, "Native methods not compiled correctly!", e);
                }
            }
        }
    }
    
    private MathUtil() {throw new Error("This mf really made an instance of a util class, what a shame");}//no instances allowed
    
    public static long percent(long number, long max) {return (number / max) * 100;}
    
    public static double percent(double number, double max) {return number / max;}
    
    public static boolean chance(double percent) {
        double i = new Random().nextDouble();
        if (percent > 1) percent = 1;
        if (percent <= 0) return false;
        return ActualSecureRandom.createSecureRandom().nextBoolean() ? i <= percent : i >= percent;
    }
    
    public static boolean chance(long percent) {
        int i = new Random().nextInt(101);
        return ActualSecureRandom.createSecureRandom().nextBoolean() ? i <= percent : i >= percent;
    }
    
    public static long randomRange(long min, long max) {
        return new Random().nextLong((max - min) + 1) + min;
    }
    
    public static double randomRange(double min, double max) {
        return new Random().nextDouble((max - min) + 1) + min;
    }
    
    public static double unExponentialFormula(double value, double divisor, double maxValue) {//great name
        if (value < 0) throw new IllegalArgumentException("value must be higher than 0");
        else if (value == 0) return 0.0;
        double output = (1-(1/(1+value/divisor))) * maxValue;
        return roundTo(output, 3);//rounding to the 0.001!
    }
    
    public static double roundTo(double value, long powerOf10) {//no way this actually works
        return Math.round(value * Math.pow(10.0, powerOf10)) / Math.pow(10.0, powerOf10);
    }
    
    public static double clamp(double value, double min, double max) {
        return Math.max(Math.min(value, max), min);
    }
    
    public static long clamp(long value, long min, long max) {
        return Math.max(Math.min(value, max), min);
    }
    
    /**
     * wrapper methods to make sure that java doesnt die if you have an unsupported os
     * or if i didnt compile it correctly
     */
    
    public static double safeSqrt(double x) {
        try {
            return fastSqrt(x);
        } catch (UnsatisfiedLinkError ignored) {
            return Math.sqrt(x);
        }
    }
    
    public static double safeInvSqrt(double x) {
        try {
            return fastInvSqrt(x);
        } catch (UnsatisfiedLinkError ignored) {
            return javaFastInvSqrt(x);
        }
    }
    
    public static float safeInvSqrt(float x) {
        try {
            return fastInvSqrt(x);
        } catch (UnsatisfiedLinkError ignored) {
            return javaFastInvSqrt(x);
        }
    }
    
    public static double safePow(double x, double b) {
        try {
            return fastPow(x, b);
        } catch (UnsatisfiedLinkError ignored) {
            return Math.pow(x, b);
        }
    }
    
    public static double safeNthRoot(double a, double x) {
        try {
            return nthRoot(a, x);
        } catch (UnsatisfiedLinkError ignored) {
            return Math.pow(x, 1 / a);
        }
    }
    
    public static native double fastSqrt(double x);//so uh Math.sqrt() uses assembly whenever possible so this is quite redundant
    
    public static native double fastInvSqrt(double x);
    
    public static native float fastInvSqrt(float x);
    
    public static native double fastPow(double num, double b);
    
    public static native double nthRoot(double a, double num);
    
    public static double javaFastInvSqrt(double x) {
        double x2 = x * 0.5;
        
        long i = Double.doubleToRawLongBits(x);
        i = 0x5fe6eb50c7b537a9L - (i >> 1);
        x = Double.longBitsToDouble(i);
        
        x = x * (1.5 - (x2 * x * x));
        x = x * (1.5 - (x2 * x * x));
        
        return x;
    }
    
    public static float javaFastInvSqrt(float x) {
        float x2 = x * 0.5F;
        
        int i = Float.floatToRawIntBits(x);
        i = 0x5f3759df - (i >> 1);
        x = Float.intBitsToFloat(i);
        
        x = x * (1.5f - (x2 * x * x));
        x = x * (1.5f - (x2 * x * x));
        
        return x;
    }
}
