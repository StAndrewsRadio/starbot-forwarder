package com.standrewsradio.starbot.forwarder;

import picocli.CommandLine;

import java.net.URL;
import java.util.Enumeration;
import java.util.jar.Attributes;
import java.util.jar.Attributes.Name;
import java.util.jar.Manifest;

/**
 * A simple version provider.
 */
public class VersionProvider implements CommandLine.IVersionProvider {

    @Override
    public String[] getVersion() throws Exception {
        Enumeration<URL> resources = CommandLine.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            Manifest manifest = new Manifest(url.openStream());

            // check if this is our jar
            if (isApplicableManifest(manifest)) {
                Attributes attributes = manifest.getMainAttributes();
                return new String[] { attributes.getValue(Name.IMPLEMENTATION_VERSION) };
            }
        }
        return new String[0];
    }

    /**
     * Checks if this manifest is our manifest.
     * @param manifest the manifest
     * @return {@code true} if this is our manifest
     */
    private boolean isApplicableManifest(Manifest manifest) {
        try {
            return manifest.getMainAttributes().getValue(Name.IMPLEMENTATION_TITLE).equals("Starbot Forwarder");
        } catch (NullPointerException ignored) {
            return false;
        }
    }
}
