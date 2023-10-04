package artoria.data;

import java.io.Serializable;

/**
 * The information object for the jar package.
 * @author Kahle
 */
public class JarInfo implements Serializable {
    private String bundleName;
    private String bundleVendor;
    private String bundleVersion;
    private String bundleDescription;

    public String getBundleName() {

        return bundleName;
    }

    public void setBundleName(String bundleName) {

        this.bundleName = bundleName;
    }

    public String getBundleVendor() {

        return bundleVendor;
    }

    public void setBundleVendor(String bundleVendor) {

        this.bundleVendor = bundleVendor;
    }

    public String getBundleVersion() {

        return bundleVersion;
    }

    public void setBundleVersion(String bundleVersion) {

        this.bundleVersion = bundleVersion;
    }

    public String getBundleDescription() {

        return bundleDescription;
    }

    public void setBundleDescription(String bundleDescription) {

        this.bundleDescription = bundleDescription;
    }

    @Override
    public String toString() {
        return "JarInfo{" +
                "bundleName='" + bundleName + '\'' +
                ", bundleVersion='" + bundleVersion + '\'' +
                ", bundleVendor='" + bundleVendor + '\'' +
                ", bundleDescription='" + bundleDescription + '\'' +
                '}';
    }

}
