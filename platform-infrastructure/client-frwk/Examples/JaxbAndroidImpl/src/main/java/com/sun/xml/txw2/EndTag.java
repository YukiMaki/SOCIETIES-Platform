/*
 * Copyright (c) 1997, 2008, Oracle and/or its affiliates. All rights reserved.
 * ORACLE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms. 
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package com.sun.xml.txw2;

/**
 * @author Kohsuke Kawaguchi
 */
final class EndTag extends Content {
    boolean concludesPendingStartTag() {
        return true;
    }

    void accept(ContentVisitor visitor) {
        visitor.onEndTag();
    }
}
