package artoria.chain;

import artoria.core.ChainNode;
import artoria.util.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ListChain extends AbstractChain {
    private final List<ChainNode> nodes;

    public ListChain(List<ChainNode> nodes) {
        Assert.notNull(nodes, "Parameter \"nodes\" must not null. ");
        this.nodes = nodes;
    }

    public ListChain() {

        this(new ArrayList<ChainNode>());
    }

    public ListChain add(ChainNode node) {
        nodes.add(node);
        return this;
    }

    public ListChain add(int index, ChainNode node) {
        nodes.add(index, node);
        return this;
    }

    public ListChain addAll(Collection<ChainNode> collection) {
        nodes.addAll(collection);
        return this;
    }

    public ListChain addAll(int index, Collection<ChainNode> collection) {
        nodes.addAll(index, collection);
        return this;
    }

    public ListChain set(int index, ChainNode node) {
        nodes.set(index, node);
        return this;
    }

    public int size() {

        return nodes.size();
    }

    @Override
    protected Iterable<ChainNode> getNodes() {

        return nodes;
    }

}
