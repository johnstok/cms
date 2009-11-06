/*-----------------------------------------------------------------------------
 * Copyright (c) 2008 Civic Computing Ltd.
 * All rights reserved.
 *
 * Revision      $Rev$
 * Modified by   $Author$
 * Modified on   $Date$
 *
 * Changes: see subversion log.
 *-----------------------------------------------------------------------------
 */

package ccc.persistence.streams;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import ccc.persistence.StreamAction;

/**
 * An action to copy from an input stream to an output stream.
 *
 * @author Civic Computing Ltd.
 */
public final class ThumbAction
    implements
        StreamAction {

    private final OutputStream _dataStream;
    private int _maxDimension = 0;

    /**
     * Constructor.
     *
     * @param dataStream The output stream to copy to.
     * @param maxDimension The maximum dimension of the image.
     */
    public ThumbAction(final OutputStream dataStream, final int maxDimension) {
        _dataStream = dataStream;
        _maxDimension = maxDimension;
    }


    /** {@inheritDoc} */
    @Override public void execute(final InputStream is) throws IOException {
        final BufferedImage image = ImageIO.read(is);
        final int w = getDisplayWidth(image.getWidth(), image.getHeight());
        final int h = getDisplayHeight(image.getWidth(), image.getHeight());

        final int type = BufferedImage.TYPE_INT_RGB;


        final BufferedImage resizedImage = new BufferedImage(w, h, type);
        final Graphics2D g = resizedImage.createGraphics();
//        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
            RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g.drawImage(image, 0, 0, w, h, null);
        g.dispose();

        ImageIO.write(resizedImage, "jpeg", _dataStream);
    }


    private int getDisplayHeight(final int width, final int height) {
        if (_maxDimension > 0
            && (width > _maxDimension || height > _maxDimension)) {
            if (height >= width) {
                return _maxDimension;
            }
            final float f = (float) height/(float) width;
            return (int) (f*_maxDimension);
        }
        return height;
    }

    private int getDisplayWidth(final int width, final int height) {
        if (_maxDimension > 0
            && (width > _maxDimension || height > _maxDimension)) {
            if (width >= height) {
                return _maxDimension;
            }
            final float f = (float) width/(float) height;
            return (int) (f*_maxDimension);
        }
        return width;
    }
}