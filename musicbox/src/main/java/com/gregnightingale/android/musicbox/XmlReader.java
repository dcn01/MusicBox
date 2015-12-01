package com.gregnightingale.android.musicbox;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gregnightingale on 11/2/15.
 */
abstract public class XmlReader {

    protected final XmlPullParser parser;

    public XmlReader(InputStream xml) throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
//        factory.setNamespaceAware(true);
        parser = factory.newPullParser();
        parser.setInput(xml, null);
    }

    protected Object read() throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                startDocument();
            } else if (eventType == XmlPullParser.START_TAG) {
                startTag();
            } else if (eventType == XmlPullParser.END_TAG) {
                endTag();
            } else if (eventType == XmlPullParser.TEXT) {
                text();
            }
            eventType = parser.next();
        }
        endDocument();
        return null;
    }

    protected abstract void startDocument();

    protected abstract void startTag();

    protected abstract void text();

    protected abstract void endTag();

    protected abstract void endDocument();

    protected Integer getIntegerAttribute(String name) {
        final String string = parser.getAttributeValue(null, name);
        if (string != null) {
            return Integer.valueOf(string);
        }
        return null;
    }

    protected String getStringAttribute(String name) {
        return parser.getAttributeValue(null, name);
    }
}
