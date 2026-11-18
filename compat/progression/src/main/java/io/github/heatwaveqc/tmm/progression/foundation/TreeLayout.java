/*
 * Adapted from HaooooZhang/Epiphany, revision 1cbffb31ef4474fec73d66f1a888b4db27c4a71d.
 * client/ui/insight/InsightTreeView.java
 * Original license: MIT; see third_party_licenses/Epiphany.txt in source and JAR.
 * Modifications: Too Much Magic contributors, 2026.
 */
package io.github.heatwaveqc.tmm.progression.foundation;

import java.util.*;

/** LDLib2-independent extraction of Epiphany's centered layers and orthogonal lines.
 * Edges are explicit server-provided prerequisites, never inferred from depth.
 * Layout is presentation only; it is not an authorization graph.
 */
public final class TreeLayout {
    private TreeLayout() {}
    public record Node(String id, int depth) {}
    public record Edge(String parent, String child) {}
    public record Point(int x, int y) {}
    public record Line(int x, int y, int width, int height) {}
    public record Layout(int width, int height, Map<String, Point> positions, List<Line> lines) {}

    public static Layout arrange(List<Node> nodes, List<Edge> edges, int nodeSize, int gap) {
        Objects.requireNonNull(nodes); Objects.requireNonNull(edges);
        if (nodeSize < 1 || nodeSize > 512 || gap < 1 || gap > 512 ||
            nodes.size() > 4096 || edges.size() > 16384)
            throw new IllegalArgumentException("Invalid dimensions or graph size");
        if (nodes.isEmpty()) {
            if (!edges.isEmpty()) throw new IllegalArgumentException("Edges without nodes");
            return new Layout(0, 0, Map.of(), List.of());
        }
        Map<Integer, List<Node>> byDepth = new TreeMap<>();
        var unique = new HashMap<String, Integer>();
        for (Node entry : nodes) {
            if (entry.id() == null || entry.id().isBlank() || entry.depth() < 0 ||
                entry.depth() > 4096 || unique.putIfAbsent(entry.id(), entry.depth()) != null)
                throw new IllegalArgumentException("Invalid or duplicate node");
            byDepth.computeIfAbsent(entry.depth(), k -> new ArrayList<>()).add(entry);
        }
        int minDepth = byDepth.keySet().stream().mapToInt(Integer::intValue).min().orElse(0);
        int maxDepth = byDepth.keySet().stream().mapToInt(Integer::intValue).max().orElse(0);
        int depthSpan = maxDepth - minDepth;
        int maxPerDepth = byDepth.values().stream().mapToInt(List::size).max().orElse(0);
        int colPitch = nodeSize + gap;
        int rowPitch = colPitch;
        int contentWidth = (maxPerDepth - 1) * colPitch + nodeSize;
        int contentHeight = (depthSpan + 1) * rowPitch;
        Map<String, Point> positions = new LinkedHashMap<>();
        for (var depthEntry : byDepth.entrySet()) {
            int y = (depthEntry.getKey() - minDepth) * rowPitch;
            var entries = depthEntry.getValue();
            int layerWidth = (entries.size() - 1) * colPitch + nodeSize;
            int layerStartX = (contentWidth - layerWidth) / 2;
            for (int col = 0; col < entries.size(); col++)
                positions.put(entries.get(col).id(), new Point(layerStartX + col * colPitch, y));
        }
        List<Line> lines = new ArrayList<>();
        Set<Edge> seen = new HashSet<>();
        for (Edge edge : edges) {
            Integer p = unique.get(edge.parent()), c = unique.get(edge.child());
            if (p == null || c == null || p >= c || !seen.add(edge))
                throw new IllegalArgumentException("Invalid or duplicate forward edge");
            Point parentPos = positions.get(edge.parent()), childPos = positions.get(edge.child());
            int pCX = parentPos.x() + nodeSize / 2;
            int pBY = parentPos.y() + nodeSize;
            int cCX = childPos.x() + nodeSize / 2;
            int cTY = childPos.y();
            int midY = pBY + (cTY - pBY) / 2;
            addLine(lines, pCX, pBY, 1, midY - pBY);
            int hs = Math.min(pCX, cCX), he = Math.max(pCX, cCX);
            addLine(lines, hs, midY, he - hs, 1);
            addLine(lines, cCX, midY, 1, cTY - midY);
        }
        return new Layout(contentWidth, contentHeight, Map.copyOf(positions), List.copyOf(lines));
    }
    private static void addLine(List<Line> lines, int x, int y, int w, int h) {
        if (w > 0 && h > 0) lines.add(new Line(x, y, w, h));
    }
}
