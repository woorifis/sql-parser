/**
 * END USER LICENSE AGREEMENT (“EULA”)
 *
 * READ THIS AGREEMENT CAREFULLY (date: 9/13/2011):
 * http://www.akiban.com/licensing/20110913
 *
 * BY INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE, YOU ARE ACCEPTING
 * ALL OF THE TERMS AND CONDITIONS OF THIS AGREEMENT. YOU AGREE THAT THIS
 * AGREEMENT IS ENFORCEABLE LIKE ANY WRITTEN AGREEMENT SIGNED BY YOU.
 *
 * IF YOU HAVE PAID A LICENSE FEE FOR USE OF THE SOFTWARE AND DO NOT AGREE TO
 * THESE TERMS, YOU MAY RETURN THE SOFTWARE FOR A FULL REFUND PROVIDED YOU (A) DO
 * NOT USE THE SOFTWARE AND (B) RETURN THE SOFTWARE WITHIN THIRTY (30) DAYS OF
 * YOUR INITIAL PURCHASE.
 *
 * IF YOU WISH TO USE THE SOFTWARE AS AN EMPLOYEE, CONTRACTOR, OR AGENT OF A
 * CORPORATION, PARTNERSHIP OR SIMILAR ENTITY, THEN YOU MUST BE AUTHORIZED TO SIGN
 * FOR AND BIND THE ENTITY IN ORDER TO ACCEPT THE TERMS OF THIS AGREEMENT. THE
 * LICENSES GRANTED UNDER THIS AGREEMENT ARE EXPRESSLY CONDITIONED UPON ACCEPTANCE
 * BY SUCH AUTHORIZED PERSONNEL.
 *
 * IF YOU HAVE ENTERED INTO A SEPARATE WRITTEN LICENSE AGREEMENT WITH AKIBAN FOR
 * USE OF THE SOFTWARE, THE TERMS AND CONDITIONS OF SUCH OTHER AGREEMENT SHALL
 * PREVAIL OVER ANY CONFLICTING TERMS OR CONDITIONS IN THIS AGREEMENT.
 */

/* The original from which this derives bore the following: */

/*

   Derby - Class org.apache.derby.impl.sql.compile.CreateViewNode

   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to you under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

 */

package com.akiban.sql.parser;

import com.akiban.sql.StandardException;

/**
 * A CreateViewNode is the root of a QueryTree that represents a CREATE VIEW
 * statement.
 *
 */

public class CreateViewNode extends DDLStatementNode
{
    // TODO: Need the rest.
    public static final int NO_CHECK_OPTION = 0;

    private ResultColumnList resultColumns;
    private ResultSetNode queryExpression;
    private String qeText;
    private int checkOption;
    private OrderByList orderByList;
    private ValueNode offset;
    private ValueNode fetchFirst;
    private ExistenceCheck existenceCheck;

    /**
     * Initializer for a CreateViewNode
     *
     * @param newObjectName The name of the table to be created
     * @param resultColumns The column list from the view definition, 
     *              if specified
     * @param queryExpression The query expression for the view
     * @param checkOption The type of WITH CHECK OPTION that was specified
     *              (NONE for now)
     * @param qeText The text for the queryExpression
     * @param orderCols ORDER BY list
     * @param offset OFFSET if any, or null
     * @param fetchFirst FETCH FIRST if any, or null
     *
     * @exception StandardException Thrown on error
     */

    public void init(Object newObjectName,
                     Object resultColumns,
                     Object queryExpression,
                     Object checkOption,
                     Object qeText,
                     Object orderCols,
                     Object offset,
                     Object fetchFirst) 
            throws StandardException {
        initAndCheck(newObjectName);
        this.resultColumns = (ResultColumnList)resultColumns;
        this.queryExpression = (ResultSetNode)queryExpression;
        this.checkOption = ((Integer)checkOption).intValue();
        this.qeText = ((String)qeText).trim();
        this.orderByList = (OrderByList)orderCols;
        this.offset = (ValueNode)offset;
        this.fetchFirst = (ValueNode)fetchFirst;

        implicitCreateSchema = true;
    }

    /**
     * Fill this node with a deep copy of the given node.
     */
    public void copyFrom(QueryTreeNode node) throws StandardException {
        super.copyFrom(node);

        CreateViewNode other = (CreateViewNode)node;
        this.resultColumns = (ResultColumnList)
            getNodeFactory().copyNode(other.resultColumns, getParserContext());
        this.queryExpression = (ResultSetNode)
            getNodeFactory().copyNode(other.queryExpression, getParserContext());
        this.qeText = other.qeText;
        this.checkOption = other.checkOption;
        this.orderByList = (OrderByList)
            getNodeFactory().copyNode(other.orderByList, getParserContext());
        this.offset = (ValueNode)
            getNodeFactory().copyNode(other.offset, getParserContext());
        this.fetchFirst = (ValueNode)
            getNodeFactory().copyNode(other.fetchFirst, getParserContext());
    }

    /**
     * Convert this object to a String.  See comments in QueryTreeNode.java
     * for how this should be done for tree printing.
     *
     * @return This object as a String
     */

    public String toString() {
        return super.toString() +
            "checkOption: " + checkOption + "\n" +
            "qeText: " + qeText + "\n";
    }

    public String statementToString() {
        return "CREATE VIEW";
    }

    /**
     * Prints the sub-nodes of this object.  See QueryTreeNode.java for
     * how tree printing is supposed to work.
     *
     * @param depth The depth of this node in the tree
     */

    public void printSubNodes(int depth) {
        super.printSubNodes(depth);

        if (resultColumns != null) {
            printLabel(depth, "resultColumns: ");
            resultColumns.treePrint(depth + 1);
        }

        printLabel(depth, "queryExpression: ");
        queryExpression.treePrint(depth + 1);
    }

    /**
     * Accept the visitor for all visitable children of this node.
     * 
     * @param v the visitor
     *
     * @exception StandardException on error
     */
    void acceptChildren(Visitor v) throws StandardException {
        super.acceptChildren(v);

        if (queryExpression != null) {
            queryExpression = (ResultSetNode)queryExpression.accept(v);
        }
    }

    public int getCheckOption() { 
        return checkOption; 
    }

    public ResultColumnList getResultColumns() {
        return resultColumns;
    }

    public ResultSetNode getParsedQueryExpression() { 
        return queryExpression; 
    }

    public OrderByList getOrderByList() {
        return orderByList;
    }

    public ValueNode getOffset() {
        return offset;
    }

    public ValueNode getFetchFirst() {
        return fetchFirst;
    }
}
