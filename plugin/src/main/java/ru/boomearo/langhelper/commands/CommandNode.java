package ru.boomearo.langhelper.commands;

import lombok.Getter;

import java.util.*;

@Getter
public abstract class CommandNode<T, D> {

    protected final CommandNode<T, D> rootNode;
    protected final String name;
    protected final List<String> aliases;

    protected final Map<String, CommandNode<T, D>> nodes = new LinkedHashMap<>();
    protected final Map<String, CommandNode<T, D>> withAliasesNodes = new LinkedHashMap<>();

    public CommandNode(CommandNode<T, D> rootNode, String name) {
        this(rootNode, name, Collections.emptyList());
    }

    public CommandNode(CommandNode<T, D> rootNode, String name, List<String> aliases) {
        this.rootNode = rootNode;
        this.name = name;
        this.aliases = aliases;
    }

    public void execute(T sender, String[] args) {
        if (!hasPermission(sender)) {
            onPermissionFailedExecute(sender, args);
            return;
        }

        if (args.length == 0) {
            onExecuteSafe(sender, args);
            return;
        }
        CommandNode<T, D> nextNode = this.withAliasesNodes.get(args[0].toLowerCase());
        if (nextNode == null) {
            onExecuteSafe(sender, args);
            return;
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        nextNode.execute(sender, newArgs);
    }

    public void onExecuteSafe(T sender, String[] args) {
        try {
            onExecute(sender, args);
        } catch (Exception e) {
            onExecuteException(sender, args, e);
        }
    }

    public Collection<String> suggest(T sender, String[] args) {
        if (!hasPermission(sender)) {
            return onPermissionFailedSuggest(sender, args);
        }
        if (args.length == 0) {
            return onSuggestSafe(sender, args);
        }

        CommandNode<T, D> nextNode = this.withAliasesNodes.get(args[0].toLowerCase());
        if (nextNode == null) {
            return onSuggestSafe(sender, args);
        }

        String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
        return nextNode.suggest(sender, newArgs);
    }

    public Collection<String> onSuggestSafe(T sender, String[] args) {
        try {
            return onSuggest(sender, args);
        } catch (Exception e) {
            return onSuggestException(sender, args, e);
        }
    }

    public void addNode(CommandNode<T, D> node) {
        this.nodes.put(node.getName().toLowerCase(), node);
        this.withAliasesNodes.put(node.getName().toLowerCase(), node);
        for (String alias : node.getAliases()) {
            this.withAliasesNodes.put(alias.toLowerCase(), node);
        }
    }

    public List<D> getDescriptionList(T sender) {
        List<D> tmp = new ArrayList<>();
        if (hasPermission(sender)) {
            List<D> description = getDescription(sender);
            if (description != null) {
                tmp.addAll(description);
            }
        }

        for (CommandNode<T, D> node : this.nodes.values()) {
            tmp.addAll(node.getDescriptionList(sender));
        }
        return tmp;
    }

    public Collection<D> getDescriptionListFromRoot(T sender) {
        if (this.rootNode == null) {
            return getDescriptionList(sender);
        }

        return this.rootNode.getDescriptionList(sender);
    }

    public boolean hasPermission(T sender) {
        return true;
    }

    public Collection<String> onSuggest(T sender, String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }
        Set<String> matches = new HashSet<>();
        String search = args[0].toLowerCase();
        for (Map.Entry<String, CommandNode<T, D>> entry : this.withAliasesNodes.entrySet()) {
            if (entry.getValue().hasPermission(sender)) {
                if (entry.getKey().toLowerCase().startsWith(search)) {
                    matches.add(entry.getKey());
                }
            }
        }
        return matches;
    }

    public abstract List<D> getDescription(T sender);

    public abstract void onExecute(T sender, String[] args);

    public abstract void onExecuteException(T sender, String[] args, Exception e);

    public abstract Collection<String> onSuggestException(T sender, String[] args, Exception e);

    public abstract void onPermissionFailedExecute(T sender, String[] args);

    public abstract Collection<String> onPermissionFailedSuggest(T sender, String[] args);

}
