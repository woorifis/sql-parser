/**
 * Copyright 2011-2013 FoundationDB, LLC
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.foundationdb.sql.parser;

import com.foundationdb.sql.StandardException;

/**
 * BEGIN / COMMIT / ROLLBACK.
 *
 */
public class TransactionControlNode extends TransactionStatementNode
{
    public static enum Operation {
        BEGIN, COMMIT, ROLLBACK
    }
    private Operation operation;

    /**
     * Initializer for a TransactionControlNode
     *
     * @param transactionOperation Type of statement.
     *
     * @exception StandardException Thrown on error
     */

    public void init(Object operation)
            throws StandardException {
        this.operation = (Operation)operation;
    }

    public Operation getOperation() {
        return operation;
    }

    /**
     * Fill this node with a deep copy of the given node.
     */
    public void copyFrom(QueryTreeNode node) throws StandardException {
        super.copyFrom(node);

        TransactionControlNode other = (TransactionControlNode)node;
        this.operation = other.operation;
    }

    /**
     * Convert this object to a String.  See comments in QueryTreeNode.java
     * for how this should be done for tree printing.
     *
     * @return This object as a String
     */

    public String toString() {
        return super.toString() + 
            "operation: " + operation + "\n";
    }

    public String statementToString() {
        switch (operation) {
        case BEGIN:
            return "BEGIN";
        case COMMIT:
            return "COMMIT";
        case ROLLBACK:
            return "ROLLBACK";
        default:
            assert false : "Unknown transaction statement type";
            return "UNKNOWN";
        }
    }

}
