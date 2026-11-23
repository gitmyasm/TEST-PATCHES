package app.template.extension.extension;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;
import org.lsposed.hiddenapibypass.HiddenApiBypass;

import java.lang.reflect.Field;
import java.util.Map;

/** Application hook that keeps signature reads consistent after re-signing. */
@SuppressWarnings({"deprecation", "unchecked"})
public class SignatureHookApplication extends Application {

    private static final String PACKAGE_PLACEHOLDER = "PACKAGE_NAME_PLACEHOLDER";
    private static final String SIGNATURE_PLACEHOLDER = "SIGNATURE_PLACEHOLDER";

    static {
        String packageName = PACKAGE_PLACEHOLDER;
        String signature = SIGNATURE_PLACEHOLDER;

        installSignatureHook(packageName, signature);
    }

    private static void installSignatureHook(String packageName, String signature) {
        try {
            Signature originalSignature = new Signature(Base64.decode(signature, Base64.DEFAULT));
            Parcelable.Creator<PackageInfo> creator = createPackageInfoCreator(packageName, originalSignature);

            Field creatorField = findField(PackageInfo.class, "CREATOR");
            creatorField.set(null, creator);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                HiddenApiBypass.addHiddenApiExemptions(
                        "Landroid/os/Parcel;",
                        "Landroid/content/pm",
                        "Landroid/app"
                );
            }
            clearPackageInfoCache();
            clearParcelCreators();
        } catch (Exception e) {
            throw new RuntimeException("Failed to install signature hook", e);
        }
    }

    private static Field findField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            Class<?> current = clazz;
            while ((current = current.getSuperclass()) != null && current != Object.class) {
                try {
                    Field field = current.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    return field;
                } catch (NoSuchFieldException ignored) {
                    // Keep walking up the hierarchy.
                }
            }
            throw e;
        }
    }

    private static Parcelable.Creator<PackageInfo> createPackageInfoCreator(
            String packageName, Signature originalSignature) {
        Parcelable.Creator<PackageInfo> defaultCreator = PackageInfo.CREATOR;
        return new Parcelable.Creator<PackageInfo>() {
            @Override
            public PackageInfo createFromParcel(Parcel source) {
                PackageInfo packageInfo = defaultCreator.createFromParcel(source);

                if (packageInfo.packageName.equals(packageName)) {
                    if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                        packageInfo.signatures[0] = originalSignature;
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && packageInfo.signingInfo != null) {
                        Signature[] signers = packageInfo.signingInfo.getApkContentsSigners();
                        if (signers != null && signers.length > 0) {
                            signers[0] = originalSignature;
                        }

                        Signature[] history = packageInfo.signingInfo.getSigningCertificateHistory();
                        if (history != null) {
                            for (int i = 0; i < history.length; i++) {
                                history[i] = originalSignature;
                            }
                        }
                    }
                }

                return packageInfo;
            }

            @Override
            public PackageInfo[] newArray(int size) {
                return defaultCreator.newArray(size);
            }
        };
    }

    private static void clearPackageInfoCache() {
        try {
            Field cacheField = findField(PackageManager.class, "sPackageInfoCache");
            Object cache = cacheField.get(null);
            if (cache != null) {
                cache.getClass().getMethod("clear").invoke(cache);
            }
        } catch (Exception ignored) {
            // Cache layout varies by Android version.
        }
    }

    private static void clearParcelCreators() {
        try {
            Map<?, ?> creators = (Map<?, ?>) findField(Parcel.class, "mCreators").get(null);
            if (creators != null) {
                creators.clear();
            }
        } catch (Exception ignored) {
            // Parcel internals vary by Android version.
        }

        try {
            Map<?, ?> pairedCreators = (Map<?, ?>) findField(Parcel.class, "sPairedCreators").get(null);
            if (pairedCreators != null) {
                pairedCreators.clear();
            }
        } catch (Exception ignored) {
            // Parcel internals vary by Android version.
        }
    }
}
