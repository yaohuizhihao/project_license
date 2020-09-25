package com.suzhuichang.project.license.utils;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.util.Iterator;

public class XMLDocument {
  private XMLNode root = null;

  protected Document doc = null;

  public XMLDocument() {}

  public XMLDocument(Reader read) {
    try {
      loadFromReader(read);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public XMLDocument(InputStream input) {
    try {
      loadFromInputStream(input);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public XMLDocument(String strFileName) {
    try {
      loadFromFile(strFileName);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void loadFromReader(Reader read) throws XMLException {
    SAXReader sBuilder = new SAXReader(false);
    try {
      this.doc = sBuilder.read(read);
      read.close();
    } catch (Exception ioE) {
      throw new XMLException(ioE);
    }
  }

  public void loadFromInputStream(InputStream input) {
    SAXReader sBuilder = new SAXReader(false);
    try {
      this.doc = sBuilder.read(input);
      input.close();
    } catch (Exception ioe) {
      ioe.printStackTrace();
    }
  }

  public void loadFromFile(String strFileName) throws IOException {
    FileInputStream fin = new FileInputStream(strFileName);
    loadFromInputStream(fin);
    this.root = null;
  }

  public void loadFromFile(File fileName) throws XMLException {
    try {
      FileInputStream fin = new FileInputStream(fileName);
      loadFromInputStream(fin);
      this.root = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void loadFromString(String str) throws XMLException {
    try {
      this.doc = DocumentHelper.parseText(str);
      this.root = null;
    } catch (DocumentException de) {
      de.printStackTrace();
    }
  }

  public void create(String rootName) throws XMLException {
    String strXML = "<?xml version=\"1.0\" encoding=\"utf-8\" ?>";
    String strRoot = rootName;
    if (strRoot.equals("")) strRoot = "Root";
    strXML = String.valueOf(strXML) + "<" + strRoot + "/>";
    try {
      this.doc = DocumentHelper.parseText(strXML);
    } catch (DocumentException dE) {
      throw new XMLException(dE);
    }
  }

  private void saveToWriter(Writer outter, String encoding) throws XMLException {
    try {
      OutputFormat format = OutputFormat.createCompactFormat();
      format.setEncoding(encoding);
      format.setNewlines(true);
      XMLWriter outputter = new XMLWriter(outter, format);
      outputter.write(this.doc);
      outter.close();
    } catch (Exception ioE) {
      throw new XMLException(ioE);
    }
  }

  public String toXML() {
    if (this.doc != null) {
      return this.doc.asXML();
    }
    return "文档格式错误";
  }

  public String asXML() {
    if (this.doc != null) {
      return this.doc.asXML();
    }
    return "文档格式错误";
  }

  public void saveToFile(String strFileName, String encoding) throws XMLException {
    File fTemp = new File(strFileName);
    if (!fTemp.exists()) {
      try {
        fTemp.createNewFile();
      } catch (IOException ioE) {
        throw new XMLException(ioE);
      }
    }
    OutputStreamWriter fWriter = null;
    try {
      FileOutputStream fOutStream = new FileOutputStream(strFileName);
      fWriter = new OutputStreamWriter(fOutStream, encoding);
    } catch (FileNotFoundException fnfE) {
      throw new XMLException(fnfE);
    } catch (IOException ioE) {
      throw new XMLException(ioE);
    }
    try {
      saveToWriter(fWriter, encoding);
    } catch (XMLException xmlE) {
      throw xmlE;
    }
  }

  public void saveToFile(String strFileName) throws XMLException {
    saveToFile(strFileName, "utf-8");
  }

  public XMLNode getRoot() {
    if (this.root == null) {
      this.root = new XMLNode(this, this.doc.getRootElement());
    }
    return this.root;
  }

  @Override
  public String toString() {
    return this.doc.asXML();
  }

  @Override
  public boolean equals(Object parm1) {
    return toString().equals(parm1.toString());
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

  public XMLNode getNode(String nodeName) {
    Element rootElement = this.doc.getRootElement();
    for (Iterator<Element> it = rootElement.elementIterator(); it.hasNext(); ) {
      Element e = it.next();
      if (nodeName.equals(e.getName())) {
        return new XMLNode(this, e);
      }
    }
    return null;
  }

  //    public XMLNode[] selectNodes(String xpath) {
  //        List<Element> lst = this.doc.selectNodes(xpath);
  //        int nCount = lst.size();
  //        XMLNode[] rets = new XMLNode[nCount];
  //        for (int i = 0; i < nCount; i++) {
  //            Element el = lst.get(i);
  //            rets[i] = new XMLNode(this, el);
  //        }
  //        return rets;
  //    }

  public XMLNode selectSingleNode(String xpath) {
    Element ele = (Element) this.doc.selectSingleNode(xpath);
    return new XMLNode(this, ele);
  }
}
