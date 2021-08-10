package com.github.manosbatsis.corda.leanstate.example.contract;

import java.lang.System;

@kotlin.Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\b\u0005\u0018\u00002\u00020\u0001:\u0002\u0015\u0016B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0012\u0010\b\u001a\u0004\u0018\u00010\t2\b\u0010\n\u001a\u0004\u0018\u00010\u000bJ\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000fH\u0016J\u001c\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u0012J\u001c\u0010\u0013\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u0012J\u001c\u0010\u0014\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u000f2\f\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\t0\u0012R\u0013\u0010\u0003\u001a\u00020\u0004\u00a2\u0006\n\n\u0002\u0010\u0007\u001a\u0004\b\u0005\u0010\u0006\u00a8\u0006\u0017"}, d2 = {"Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract;", "Lnet/corda/core/contracts/Contract;", "()V", "contractStateType", "error/NonExistentClass", "getContractStateType", "()Lerror/NonExistentClass;", "Lerror/NonExistentClass;", "owningKey", "Ljava/security/PublicKey;", "party", "Lnet/corda/core/identity/Party;", "verify", "", "tx", "Lnet/corda/core/transactions/LedgerTransaction;", "verifyCreate", "signers", "", "verifyDelete", "verifyUpdate", "Commands", "NewsPaper", "leanstate-example-contract"})
public final class NewsPaperContract implements net.corda.core.contracts.Contract {
    @org.jetbrains.annotations.NotNull()
    private final error.NonExistentClass contractStateType = null;
    
    @org.jetbrains.annotations.NotNull()
    public final error.NonExistentClass getContractStateType() {
        return null;
    }
    
    /**
     * * Verify transactions
     */
    @java.lang.Override()
    public void verify(@org.jetbrains.annotations.NotNull()
    net.corda.core.transactions.LedgerTransaction tx) {
    }
    
    public final void verifyCreate(@org.jetbrains.annotations.NotNull()
    net.corda.core.transactions.LedgerTransaction tx, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends java.security.PublicKey> signers) {
    }
    
    public final void verifyUpdate(@org.jetbrains.annotations.NotNull()
    net.corda.core.transactions.LedgerTransaction tx, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends java.security.PublicKey> signers) {
    }
    
    public final void verifyDelete(@org.jetbrains.annotations.NotNull()
    net.corda.core.transactions.LedgerTransaction tx, @org.jetbrains.annotations.NotNull()
    java.util.Set<? extends java.security.PublicKey> signers) {
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.security.PublicKey owningKey(@org.jetbrains.annotations.Nullable()
    net.corda.core.identity.Party party) {
        return null;
    }
    
    public NewsPaperContract() {
        super();
    }
    
    /**
     * * Contract commands
     */
    @kotlin.Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001:\u0003\u0002\u0003\u0004\u00a8\u0006\u0005"}, d2 = {"Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract$Commands;", "Lnet/corda/core/contracts/CommandData;", "Create", "Delete", "Update", "leanstate-example-contract"})
    public static abstract interface Commands extends net.corda.core.contracts.CommandData {
        
        /**
         * Create the initial state 
         */
        @kotlin.Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract$Commands$Create;", "Lnet/corda/core/contracts/TypeOnlyCommandData;", "Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract$Commands;", "()V", "leanstate-example-contract"})
        public static final class Create extends net.corda.core.contracts.TypeOnlyCommandData implements com.github.manosbatsis.corda.leanstate.example.contract.NewsPaperContract.Commands {
            
            public Create() {
                super();
            }
        }
        
        /**
         * Create the updated state 
         */
        @kotlin.Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract$Commands$Update;", "Lnet/corda/core/contracts/TypeOnlyCommandData;", "Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract$Commands;", "()V", "leanstate-example-contract"})
        public static final class Update extends net.corda.core.contracts.TypeOnlyCommandData implements com.github.manosbatsis.corda.leanstate.example.contract.NewsPaperContract.Commands {
            
            public Update() {
                super();
            }
        }
        
        /**
         * Delete the state 
         */
        @kotlin.Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\u0000\u0010\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u0002B\u0005\u00a2\u0006\u0002\u0010\u0003\u00a8\u0006\u0004"}, d2 = {"Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract$Commands$Delete;", "Lnet/corda/core/contracts/TypeOnlyCommandData;", "Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract$Commands;", "()V", "leanstate-example-contract"})
        public static final class Delete extends net.corda.core.contracts.TypeOnlyCommandData implements com.github.manosbatsis.corda.leanstate.example.contract.NewsPaperContract.Commands {
            
            public Delete() {
                super();
            }
        }
    }
    
    @com.github.manosbatsis.corda.leanstate.annotation.LeanStateModel(mappingModes = {com.github.manosbatsis.corda.leanstate.annotation.PropertyMappingMode.NATIVE, com.github.manosbatsis.corda.leanstate.annotation.PropertyMappingMode.STRINGIFY, com.github.manosbatsis.corda.leanstate.annotation.PropertyMappingMode.EXPANDED})
    @kotlin.Metadata(mv = {1, 1, 11}, bv = {1, 0, 2}, k = 1, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0007\bf\u0018\u00002\u00020\u0001R\u0016\u0010\u0002\u001a\u0004\u0018\u00010\u00038\'X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0012\u0010\u0012\u001a\u00020\u0013X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015R\u0014\u0010\u0016\u001a\u0004\u0018\u00010\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0017\u0010\tR\u0012\u0010\u0018\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0019\u0010\u0005\u00a8\u0006\u001a"}, d2 = {"Lcom/github/manosbatsis/corda/leanstate/example/contract/NewsPaperContract$NewsPaper;", "", "alternativeTitle", "", "getAlternativeTitle", "()Ljava/lang/String;", "author", "Lnet/corda/core/identity/Party;", "getAuthor", "()Lnet/corda/core/identity/Party;", "editions", "", "getEditions", "()I", "price", "Ljava/math/BigDecimal;", "getPrice", "()Ljava/math/BigDecimal;", "published", "Ljava/util/Date;", "getPublished", "()Ljava/util/Date;", "publisher", "getPublisher", "title", "getTitle", "leanstate-example-contract"})
    public static abstract interface NewsPaper {
        
        @org.jetbrains.annotations.Nullable()
        public abstract net.corda.core.identity.Party getPublisher();
        
        @org.jetbrains.annotations.NotNull()
        public abstract net.corda.core.identity.Party getAuthor();
        
        @org.jetbrains.annotations.NotNull()
        public abstract java.math.BigDecimal getPrice();
        
        @com.github.manosbatsis.corda.leanstate.annotation.LeanStateProperty(initializer = "1")
        public abstract int getEditions();
        
        @org.jetbrains.annotations.NotNull()
        public abstract java.lang.String getTitle();
        
        @org.jetbrains.annotations.NotNull()
        @com.github.manosbatsis.corda.leanstate.annotation.LeanStateProperty(initializer = "Date()")
        public abstract java.util.Date getPublished();
        
        @org.jetbrains.annotations.Nullable()
        @javax.persistence.Column(name = "alt_title", length = 500)
        public abstract java.lang.String getAlternativeTitle();
    }
}