package org.societies.api.internal.orchestration;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Bjørn Magnus Mathisen
 * Date: 19.09.12
 * Time: 16:06
 */
public interface ISocialGraph {
    public List<ISocialGraphEdge> getEdges();
    public List<ISocialGraphVertex> getVertices();
}
