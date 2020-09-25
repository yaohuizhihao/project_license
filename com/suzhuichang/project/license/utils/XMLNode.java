package com.suzhuichang.project.license.utils;

import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XMLNode {
  private Element srcElement;

  private XMLDocument doc;

  Element getSourceElement() {
    return this.srcElement;
  }

  XMLDocument getdoc() {
    return this.doc;
  }

  public XMLNode(XMLDocument doc, Element ele) {
    this.doc = doc;
    this.srcElement = ele;
  }

  public XMLDocument getDocument() {
    return this.doc;
  }

  public Object getSource() {
    return this;
  }

  public int getChildNodesCount() {
    return this.srcElement.elements().size();
  }

  public XMLNode getParent() {
    Element elParent = this.srcElement.getParent();
    if (elParent != null) {
      return new XMLNode(this.doc, elParent);
    }
    return null;
  }

  public XMLNode getChildNode(int nIndex) {
    if (nIndex >= 0 && nIndex < getChildNodesCount()) {
      List<Element> lst = this.srcElement.elements();
      Element elChild = lst.get(nIndex);
      return new XMLNode(this.doc, elChild);
    }
    return null;
  }

  public XMLNode getChildNode(String nodeName) {
    Element elChild = this.srcElement.element(nodeName);
    if (elChild != null) {
      return new XMLNode(this.doc, elChild);
    }
    return null;
  }

  public XMLNode[] getAllChildNode() {
    ArrayList<XMLNode> list = new ArrayList();
    for (Iterator<Element> it = this.srcElement.elementIterator(); it.hasNext(); ) {
      Element elChild = it.next();
      list.add(new XMLNode(this.doc, elChild));
    }
    return list.<XMLNode>toArray(new XMLNode[list.size()]);
  }

  public XMLNode[] getAllChildNode(String nodeName) {
    ArrayList<XMLNode> list = new ArrayList();
    for (Iterator<Element> it = this.srcElement.elementIterator(); it.hasNext(); ) {
      Element elChild = it.next();
      if (nodeName.equals(elChild.getName())) list.add(new XMLNode(this.doc, elChild));
    }
    return list.<XMLNode>toArray(new XMLNode[list.size()]);
  }

  public XMLNode createChildNode(String nodeName) {
    Element elNew = this.srcElement.addElement(nodeName);
    return new XMLNode(this.doc, elNew);
  }

  public XMLNode newNode(String nodeName) {
    Element ele = DocumentHelper.createElement(nodeName);
    return new XMLNode(this.doc, ele);
  }

  public XMLNode addChildNode(XMLNode newNode) {
    Element elNew = newNode.getSourceElement();
    this.srcElement.appendContent((Branch) elNew);
    return new XMLNode(this.doc, elNew);
  }

  public void copy(XMLNode source) {
    String[] attrNames = source.getAttributeNames();
    removeAttributes();
    for (int i = 0; i < attrNames.length; i++) {
      setAttribute(attrNames[i], source.getAttributeValue(attrNames[i]));
    }
    int nCount = source.getChildNodesCount();
    if (nCount == 0) {
      String nodeValue = source.getNodeValue();
      if (!nodeValue.equals("")) {
        setText(nodeValue);
      }
    } else {
      for (int j = 0; j < nCount; j++) {
        XMLNode subSource = source.getChildNode(j);
        XMLNode subTarget = createChildNode(subSource.getNodeName());
        subTarget.copy(subSource);
      }
    }
  }

  public void removeChildren() {
    this.srcElement.clearContent();
  }

  public void removeAttributes() {
    String[] attrNames = getAttributeNames();
    for (int i = 0; i < attrNames.length; i++) {
      removeAttribute(attrNames[i]);
    }
  }

  public void removeChildNode(String nodeName) {
    this.srcElement.remove(this.srcElement.element(nodeName));
  }

  public void removeChildNode(XMLNode node) {
    XMLNode doc = (XMLNode) node.getSource();
    Element elNode = doc.getSourceElement();
    this.srcElement.remove(elNode);
  }

  public void removeAllChildNode(String nodeName) {
    List nodes = this.srcElement.elements(nodeName);
    for (Iterator<Element> it = nodes.iterator(); it.hasNext(); ) {
      Element elm = it.next();
      this.srcElement.remove(elm);
    }
  }

  public void removeAllChildNode(XMLNode node) {
    removeAllChildNode(node.getNodeName());
  }

  public String getNodeName() {
    return this.srcElement.getName();
  }

  public String getNodeValue() {
    return this.srcElement.getTextTrim();
  }

  public void setText(String nodeValue) {
    this.srcElement.clearContent();
    this.srcElement.setText(nodeValue);
  }

  public void setCDATAText(String cdataValue) {
    this.srcElement.clearContent();
    this.srcElement.addCDATA(cdataValue);
  }

  public int getAttributesCount() {
    return this.srcElement.attributeCount();
  }

  public String[] getAttributeNames() {
    List lst = this.srcElement.attributes();
    String[] Ret = new String[lst.size()];
    Iterator<Attribute> item = lst.iterator();
    int i = 0;
    while (item.hasNext()) {
      Attribute attr = item.next();
      Ret[i] = attr.getName();
      i++;
    }
    return Ret;
  }

  public String getAttributeValue(String attrName) {
    Attribute attr = this.srcElement.attribute(attrName);
    if (attr != null) {
      String retValue = attr.getValue();
      if (retValue == null) {
        return "";
      }
      return retValue;
    }
    return "";
  }

  public void setAttribute(String attrName, String attrValue) {
    addAttribute(attrName, attrValue);
  }

  public void addAttribute(String attrName, String attrValue) {
    try {
      this.srcElement.addAttribute(attrName, attrValue);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void removeAttribute(String attrName) {
    try {
      this.srcElement.remove(this.srcElement.attribute(attrName));
    } catch (Exception exception) {
    }
  }

  //    public XMLNode[] selectNodes(String xpath) {
  //        List<Element> lst = this.srcElement.selectNodes(xpath);
  //        int nCount = lst.size();
  //        XMLNode[] rets = new XMLNode[nCount];
  //        for (int i = 0; i < nCount; i++) {
  //            Element el = lst.get(i);
  //            rets[i] = new XMLNode(this.doc, el);
  //        }
  //        return rets;
  //    }

  public void setNodeName(String nodeName) {
    this.srcElement.setName(nodeName);
  }

  public void insertBeforeNode(XMLNode newNode) {
    List<Element> list = this.srcElement.getParent().elements();
    list.add(list.indexOf(this.srcElement), newNode.srcElement);
  }

  public void insertBeforeNode(XMLNode refNode, XMLNode newNode) {
    List<Element> list = refNode.srcElement.getParent().elements();
    list.add(list.indexOf(refNode.srcElement), newNode.srcElement);
  }

  public void insertAfterNode(XMLNode newNode) {
    List<Element> list = this.srcElement.getParent().elements();
    list.add(list.indexOf(this.srcElement) + 1, newNode.srcElement);
  }

  public void insertAfterNode(XMLNode refNode, XMLNode newNode) {
    List<Element> list = refNode.srcElement.getParent().elements();
    list.add(list.indexOf(refNode.srcElement) + 1, newNode.srcElement);
  }
}
