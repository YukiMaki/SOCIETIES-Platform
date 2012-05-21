package org.societies.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ProviderElementNamespaceRegistrar {
	
	private Map<ElementNamespaceTuple, Integer> providersElementsAndNamespaces = new HashMap<ElementNamespaceTuple, Integer>();
			
	public void register(ElementNamespaceTuple tuple) {
		Integer count = providersElementsAndNamespaces.get(tuple);
		if(count == null) {
			count = 1;
		}
		else {
			count++;
		}
		providersElementsAndNamespaces.put(tuple, count);			
	}
	
	public void unregister(ElementNamespaceTuple tuple) {		
		Integer count = providersElementsAndNamespaces.get(tuple);
		count--;
		if(count == 0)
			providersElementsAndNamespaces.remove(tuple);	
		else
			providersElementsAndNamespaces.put(tuple, count);	
	}
	
	public boolean isRegistered(ElementNamespaceTuple tuple) {
		return providersElementsAndNamespaces.keySet().contains(tuple);
	}
	
	public void clear() {
		providersElementsAndNamespaces.clear();
	}
	
	public Set<ElementNamespaceTuple> getRegists() {
		return providersElementsAndNamespaces.keySet();
	}
			
	public static class ElementNamespaceTuple {
		public String elementName;
		public String namespace;
		
		public ElementNamespaceTuple(String elementName, String namespace) {
			this.elementName = elementName;
			this.namespace = namespace;
		}

		@Override
		public boolean equals(Object obj) {
			if(obj == this)
				return true;
			if(!(obj instanceof ElementNamespaceTuple))
				return false;
			ElementNamespaceTuple other = (ElementNamespaceTuple) obj;
			return other.elementName.equals(elementName) && other.namespace.equals(namespace);
		}

		@Override
		public int hashCode() {
			return elementName.hashCode() ^ namespace.hashCode();
		}	
	}
}
