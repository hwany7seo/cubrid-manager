/*
 * Copyright (C) 2009 Search Solution Corporation. All rights reserved by Search
 * Solution.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met: -
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer. - Redistributions in binary
 * form must reproduce the above copyright notice, this list of conditions and
 * the following disclaimer in the documentation and/or other materials provided
 * with the distribution. - Neither the name of the <ORGANIZATION> nor the names
 * of its contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
package com.cubrid.cubridmanager.plugin;

import com.cubrid.common.core.util.ApplicationType;
import com.cubrid.common.core.util.ApplicationUtil;
import com.cubrid.common.ui.spi.persist.CubridJdbcManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 *
 * @author pangqiren
 * @version 1.0 - 2009-12-23 created by pangqiren
 */
public class CubridManagerPlugin extends AbstractUIPlugin {

    // The plug-in ID
    public static final String PLUGIN_ID = "com.cubrid.cubridmanager.plugin";

    // The shared instance
    private static CubridManagerPlugin plugin;

    /**
     * Call this method when plugin start
     *
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
     * @param context the bundle context for this plug-in
     * @exception Exception if this plug-in did not start up properly
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
        ActionBuilder.getInstance();
        ApplicationUtil.setApplicationType(ApplicationType.CUBRID_MANAGER);
        CubridJdbcManager.getInstance();
    }

    /**
     * Call this method when plugin stop
     *
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
     * @param context the bundle context for this plug-in
     * @exception Exception if this plug-in did not start up properly
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static CubridManagerPlugin getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     *
     * @param path the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        ImageDescriptor imageDesc = getDefault().getImageRegistry().getDescriptor(path);
        if (imageDesc == null) {
            imageDesc = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
            CubridManagerPlugin.getDefault().getImageRegistry().put(path, imageDesc);
        }
        return imageDesc;
    }

    /**
     * Returns an image for the image file at the given plug-in relative path.
     *
     * @param path the path
     * @return the image
     */
    public static Image getImage(String path) {
        Image image = getDefault().getImageRegistry().get(path);
        if (image == null || image.isDisposed()) {
            ImageDescriptor imageDesc = AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_ID, path);
            CubridManagerPlugin.getDefault().getImageRegistry().put(path, imageDesc);
            return CubridManagerPlugin.getDefault().getImageRegistry().get(path);
        }
        return image;
    }
}
