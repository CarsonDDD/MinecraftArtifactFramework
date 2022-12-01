package me.emprzedd.artifactframework;

import org.bukkit.attribute.AttributeModifier;

// This class is useless. However, the way in which im using Attribute Modifier's as a key is extremely jank.
// This helper class allows me to easily refactor things if (when) I ever need to change the way I handle
// persistency and artifact identification.
public class ArtifactKey{
    public AttributeModifier modifier;// dirty public because this isn't a real solution

    public ArtifactKey(AttributeModifier modifier){
        this.modifier = modifier;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof ArtifactKey)) return false;
        //return modifier.equals(((ArtifactKey)o).modifier);

        AttributeModifier other = ((ArtifactKey) o).modifier;
        boolean slots = (modifier.getSlot() != null ? (modifier.getSlot() == other.getSlot()) : other.getSlot() == null);
        return modifier.getName().equals(other.getName()) && modifier.getAmount() == other.getAmount() && modifier.getOperation() == other.getOperation() && slots;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((modifier == null) ? 0 : modifier.getName().hashCode());
        return result;
    }


}