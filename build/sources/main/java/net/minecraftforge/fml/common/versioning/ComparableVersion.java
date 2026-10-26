package net.minecraftforge.fml.common.versioning;
public class ComparableVersion implements Comparable<ComparableVersion> {
    private final String version;
    public ComparableVersion(String version) { this.version = version; }
    public int compareTo(ComparableVersion o) { return this.version.compareTo(o.version); }
}