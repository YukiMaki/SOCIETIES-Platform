/**
 * Copyright (c) 2011, SOCIETIES Consortium
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 *    disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.societies.privacytrust.privacyprotection.api.model.privacypreference;

import java.io.Serializable;
import java.util.Enumeration;

import javax.swing.tree.MutableTreeNode;
/**
 * @author Elizabeth
 *
 */
public interface IPrivacyPreference extends MutableTreeNode, Serializable{
	public IPrivacyPreferenceCondition getCondition();
	public IPrivacyOutcome getOutcome();
	public boolean isLeaf();
	public boolean isBranch();
	public Object[] getUserObjectPath();
	public Object getUserObject();
	public void add(IPrivacyPreference p);
	public void remove(IPrivacyPreference p);
	public Enumeration<IPrivacyPreference> depthFirstEnumeration();
	public Enumeration<IPrivacyPreference> breadthFirstEnumeration();
	public Enumeration<IPrivacyPreference> postorderEnumeration();
	public Enumeration<IPrivacyPreference> preorderEnumeration();
	public IPrivacyPreference getRoot();
	public int getLevel();
	public int getDepth();
}