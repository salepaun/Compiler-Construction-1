package rs.ac.bg.etf.pp1;

import java.util.*;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

public class SemanticAnalyzer extends VisitorAdaptor {

	public static final Struct boolType = new Struct(Struct.Bool);
	public Type currentType = null;
	public Obj currentClass = null;
	public Obj currentMethod = null;
	public Type currentMethodType = null;
	public boolean isCorrect = true;
	public boolean hasFoundReturn = false;
	public boolean hasFoundMain = false;
	public boolean hasArgs = false;
	public boolean isArray = false;

	public List<Struct> paramList = new LinkedList<>();

	public Stack<ScopeEnum> scopeStack = new Stack<>();

	public enum ScopeEnum {
		GLOBAL, LOCAL, CLASS
	}

	boolean isFirstDeclaration(String name, int line) {
		if (Tab.currentScope.findSymbol(name) == null)
			return true;
		else {
			System.out.println("Greska na liniji " + line + " - promenljiva " + name + " je vec deklarisana");
			return false;
		}
	}

	void printObj(Obj o) {
		System.out.println(String.format("Name %s StructKind: %d", o.getName(), o.getType().getKind()));
	}

	public String StructToString(Struct type) {
		switch (type.getKind()) {
		case Struct.Array:
			return StructToString(type.getElemType()) + "[]";
		case Struct.Bool:
			return "bool";
		case Struct.Char:
			return "char";
		case Struct.Int:
			return "int";
		case Struct.None:
			return "none";
		default:
			return "default";
		}
	}

	public SemanticAnalyzer() {
		Tab.init();
		Tab.currentScope.addToLocals(new Obj(Obj.Type, "bool", boolType));
		scopeStack.push(ScopeEnum.GLOBAL);
		isCorrect = true;
	}

	public void push(ScopeEnum scope) {
		scopeStack.push(scope);
	}

	public ScopeEnum peek() {
		return scopeStack.peek();
	}

	public ScopeEnum pop() {
		return scopeStack.pop();
	}

	@Override
	public void visit(Mulop Mulop) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Relop Relop) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Addop Addop) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(PrintExprOnly PrintExprOnly) {
		Expr expr = PrintExprOnly.getExpr();
		if (expr.struct != Tab.intType && expr.struct != Tab.charType && expr.struct != boolType) {
			System.out.println("PrintExprOnly moze da ispise samo int ili char a ne " + expr.struct.getKind()
					+ " na liniji " + PrintExprOnly.getLine());
			isCorrect = false;
			return;
		}
	}

	@Override
	public void visit(Factor Factor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CondTerm CondTerm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Designator Designator) {
	}

	@Override
	public void visit(Term Term) {
		if (Term instanceof TermList) {
			TermList list = (TermList) Term;
			Term term = list.getTerm();
			Factor factor = list.getFactor();
			if (term.struct != Tab.intType || factor.struct != Tab.intType) {
				isCorrect = false;
				System.out.println("TermList nije int tipa");
				return;
			}
			list.struct = Tab.intType;
		} else {
			TermFactor termFactor = (TermFactor) Term;
			/*
			 * if (termFactor.getFactor().struct != Tab.intType) { isCorrect = false;
			 * System.out.println("TermFactor nije int tipa"); return; }
			 */
			termFactor.struct = termFactor.getFactor().struct;
		}
	}

	@Override
	public void visit(Condition Condition) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ConstValue ConstValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ExtendsDecl ExtendsDecl) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ActualParamList ActualParamList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(VarDeclList VarDeclList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FormalParamList FormalParamList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DesignatorStatement DesignatorStatement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ActualPars ActualPars) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(Statement Statement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CondFact CondFact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MethodDeclList MethodDeclList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FormPars FormPars) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MulopMod MulopMod) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MulopDiv MulopDiv) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(MulopMul MulopMul) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AddopMinus AddopMinus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(AddopPlus AddopPlus) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(RelopLessOrEqual RelopLessOrEqual) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(RelopLess RelopLess) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(RelopGreaterOrEqual RelopGreaterOrEqual) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(RelopGreater RelopGreater) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(RelopNotEqual RelopNotEqual) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(RelopIsEqual RelopIsEqual) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ReturnExpr ReturnExpr) {
		if (currentMethod == null) {
			System.out.println("Greska, return iskaz nije u metodi");
			isCorrect = false;
			return;
		}
		if (currentMethodType == null) {
			System.out.println("Greska, currentMethodType je null");
			isCorrect = false;
			return;
		}
		if (currentMethodType.getTypeName().equals("void")) {
			System.out.println("Greska, void metoda ne moze da vraca iskaz");
			isCorrect = false;
			return;
		}

		if (ReturnExpr.getExpr().struct.getKind() != currentMethodType.struct.getKind()) {
			System.out.println("Greska, ne slaze se tip metode i povratna vrednost");
			isCorrect = false;
			return;
		}
		hasFoundReturn = true;
		// System.out.println("Tip metode i povratni iskaz se slazu");
	}

	@Override
	public void visit(ReturnVoid ReturnVoid) {
		if (!currentMethodType.getTypeName().equals("void")) {
			System.out
					.println("Greska, metoda tipa " + currentMethodType.getTypeName() + " mora da ima povratni iskaz");
			isCorrect = false;
			return;
		}
	}

	@Override
	public void visit(DesignatorArray DesignatorArray) {
		Designator designator = DesignatorArray.getDesignator();
		Expr expr = DesignatorArray.getExpr();
		if (designator.obj != Tab.noObj) {
			if (designator.obj.getType().getKind() != Struct.Array) {
				System.out.println("DesignatorArray bi trebalo da je niz");
				isCorrect = false;
				DesignatorArray.obj = Tab.noObj;
				return;
			}
		}

		if (expr.struct.getKind() != Struct.Int) {
			System.out.println("Expr u DesignatorArray bi trebalo da je int");
			isCorrect = false;
			DesignatorArray.obj = Tab.noObj;
			return;
		}

		DesignatorArray.obj = new Obj(Obj.Elem, designator.obj.getName() + "[]",
				designator.obj.getType().getElemType());
	}

	@Override
	public void visit(DesignatorAccessMember DesignatorAccessMember) {
		Designator designator = DesignatorAccessMember.getDesignator();

		DesignatorAccessMember.obj = designator.obj;
	}

	@Override
	public void visit(DesignatorIdent DesignatorIdent) {
		Obj obj = Tab.find(DesignatorIdent.getName());
		if (obj == Tab.noObj) {
			System.out.println("Designator " + DesignatorIdent.getName() + " ne postoji u tabeli na liniji "
					+ DesignatorIdent.getLine());
			isCorrect = false;
		}
		DesignatorIdent.obj = obj;
	}

	@Override
	public void visit(CondFactExpr CondFactExpr) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CondFactRelopList CondFactRelopList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CondTermCondFact CondTermCondFact) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(CondTermAndList CondTermAndList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ConditionCondTerm ConditionCondTerm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ConditionOrList ConditionOrList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ActualParamsListExpr ActualParamsListExpr) {
		Expr expr = ActualParamsListExpr.getExpr();
		paramList.add(0, expr.struct);
	}

	@Override
	public void visit(ActualParamsList ActualParamsList) {
		Expr expr = ActualParamsList.getExpr();
		paramList.add(0, expr.struct);
	}

	@Override
	public void visit(NoActualParsList NoActualParsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(ActualParsList ActualParsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FactorExpr FactorExpr) {
		FactorExpr.struct = FactorExpr.getExpr().struct;
	}

	@Override
	public void visit(FactorNew FactorNew) {

	}

	@Override
	public void visit(FactorNewArray FactorNewArray) {
		Type type = FactorNewArray.getType();
		Expr expr = FactorNewArray.getExpr();

		if (expr.struct != Tab.intType) {
			System.out.println("Factor new array, expr nije int tipa");
			isCorrect = false;
			return;
		}

		Struct arrayType = new Struct(Struct.Array, type.struct);
		FactorNewArray.struct = arrayType;
	}

	@Override
	public void visit(FactorConstValue FactorConstValue) {
		ConstValue constValue = FactorConstValue.getConstValue();
		if (constValue instanceof NumConst) {
			FactorConstValue.struct = Tab.intType;
		} else if (constValue instanceof CharConst) {
			FactorConstValue.struct = Tab.charType;
		} else if (constValue instanceof BoolConst) {
			FactorConstValue.struct = boolType;
		} else {
			System.out.println("FactorConstValue error");
			isCorrect = false;
			return;
		}

	}

	@Override
	public void visit(FactorDesignatorFuncCall FactorDesignatorFuncCall) {
		Designator designator = FactorDesignatorFuncCall.getDesignator();
		if (designator.obj.getKind() != Obj.Meth) {
			System.out.println("Designator mora biti tipa meth");
			isCorrect = false;
			return;
		}
		FactorDesignatorFuncCall.struct = designator.obj.getType();
		// TODO provera actualpars

		List<Struct> formalParams = new ArrayList<>();
		Collection<Obj> localSymbols = designator.obj.getLocalSymbols();
		int numParams = designator.obj.getLevel();
//		for(int i = 0; i < numParams;i++) {
//			formalParams.add(localSymbols[i].getType());
//		}
		int cnt = 0;
		for (Obj obj : localSymbols) {
			if (cnt < numParams) {
				formalParams.add(obj.getType());
				cnt++;
			}
			else break;
		}

		if (formalParams.size() != paramList.size()) {
			System.out.println("Liste nisu iste duzine na liniji " + FactorDesignatorFuncCall.getLine());
		} else {
			for (int i = 0; i < paramList.size(); i++) {
				if (!formalParams.get(i).compatibleWith(paramList.get(i))) {
					System.out.println(
							"Tipovi nisu kompatibilni index " + i + " na liniji " + FactorDesignatorFuncCall.getLine());
					isCorrect = false;
				}
			}
		}

		paramList.clear();
		System.out.println("Prosao poziv funkcije " + designator.obj.getName() + " na liniji "
				+ FactorDesignatorFuncCall.getLine());
	}

	@Override
	public void visit(FactorDesignator FactorDesignator) {
		Designator designator = FactorDesignator.getDesignator();
		// printObj(designator.obj);
		if (designator.obj.getName().equals("null")) {
			FactorDesignator.struct = Tab.nullType;
			return;
		}
		if (designator.obj.getType().getKind() == Struct.Array) {
			FactorDesignator.struct = designator.obj.getType().getElemType();
			return;
		}
		// if (designator.obj.getKind() != Obj.Con && designator.obj.getKind() !=
		// Obj.Fld
		// && designator.obj.getKind() != Obj.Var && designator.obj.getKind() != ARG) {
		if (Obj.Meth == designator.obj.getKind() || Obj.Prog == designator.obj.getKind()
				|| Obj.Type == designator.obj.getKind()) {
			System.out.println("Designator mora biti tipa con/var/fld/arg/arr na liniji " + FactorDesignator.getLine());
			isCorrect = false;
			return;
		}
		FactorDesignator.struct = designator.obj.getType();
	}

	@Override
	public void visit(TermFactor TermFactor) {
		Factor factor = TermFactor.getFactor();
		TermFactor.struct = factor.struct;
	}

	@Override
	public void visit(TermList TermList) {
		Term term = TermList.getTerm();
		Factor factor = TermList.getFactor();

		if (!term.struct.equals(factor.struct) || !term.struct.equals(Tab.intType)) {
			System.out.println("Term ili factor nisu tipa int");
			isCorrect = false;

			TermList.struct = Tab.nullType;
			return;
		}

		TermList.struct = Tab.intType;
	}

	@Override
	public void visit(ExprTerm ExprTerm) {
		Term term = ExprTerm.getTerm();
		ExprTerm.struct = term.struct;
	}

	@Override
	public void visit(ExprNegTerm ExprNegTerm) {
		Term term = ExprNegTerm.getTerm();

		if (term.struct != Tab.intType) {
			System.out.println("-Term mora biti tipa int");
			isCorrect = false;
			ExprNegTerm.struct = Tab.nullType;
			return;
		}
		ExprNegTerm.struct = Tab.intType;
	}

	@Override
	public void visit(ExprList ExprList) {
		Expr expr = ExprList.getExpr();
		Term term = ExprList.getTerm();

		if (!expr.struct.equals(term.struct) || expr.struct != Tab.intType) {
			System.out.println("Expr i term moraju biti tipa int");
			isCorrect = false;
			ExprList.struct = Tab.nullType;
			return;
		}
		ExprList.struct = Tab.intType;
	}

	@Override
	public void visit(DesignatorDec DesignatorDec) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DesignatorInc DesignatorInc) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DesignatorFuncCall DesignatorFuncCall) {
		Designator designator = DesignatorFuncCall.getDesignator();
		if (designator.obj.getKind() != Obj.Meth) {
			System.out.println("Designator mora biti tipa meth");
			isCorrect = false;
			return;
		}
		List<Struct> formalParams = new ArrayList<>();
		Collection<Obj> localSymbols = designator.obj.getLocalSymbols();
		int numParams = designator.obj.getLevel();
//		for(int i = 0; i < numParams;i++) {
//			formalParams.add(localSymbols[i].getType());
//		}
		int cnt = 0;
		for (Obj obj : localSymbols) {
			if (cnt < numParams) {
				formalParams.add(obj.getType());
				cnt++;
			}
			else break;
		}

		if (formalParams.size() != paramList.size()) {
			System.out.println("Liste nisu iste duzine na liniji " + DesignatorFuncCall.getLine());
		} else {
			for (int i = 0; i < paramList.size(); i++) {
				if (!formalParams.get(i).compatibleWith(paramList.get(i))) {
					System.out.println(
							"Tipovi nisu kompatibilni index " + i + " na liniji " + DesignatorFuncCall.getLine());
					isCorrect = false;
				}
			}
		}

		paramList.clear();
		System.out.println("Poziv " + designator.obj.getName() + " na liniji " + DesignatorFuncCall.getLine());

	}

	@Override
	public void visit(DesignatorAssigment DesignatorAssigment) {
		Designator designator = DesignatorAssigment.getDesignator();
		Expr expr = DesignatorAssigment.getExpr();

		if (!expr.struct.assignableTo(designator.obj.getType())) {
			System.out.println("Expr ne moze se dodeliti designatoru " + designator.obj.getName() + " na liniji "
					+ DesignatorAssigment.getLine());
			isCorrect = false;
		}

	}

	@Override
	public void visit(BraceStatementList BraceStatementList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(DesignatorSingleStatement DesignatorSingleStatement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NoStatementsList NoStatementsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(StatementsList StatementsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(FormalParamDecl FormalParamDecl) {
		Type type = FormalParamDecl.getType();
		String name = FormalParamDecl.getName();
		Struct argType = type.struct;

		if (isArray) {
			argType = new Struct(Struct.Array, type.struct);
		}

		Tab.insert(Obj.Var, name, argType);
		System.out.println("Formalni parametar metode " + currentMethod.getName() + ": " + name + " "
				+ type.getTypeName() + (isArray ? "[]" : "") + " na liniji " + FormalParamDecl.getLine());
		currentMethod.setLevel(currentMethod.getLevel() + 1);
	}

	@Override
	public void visit(FormalParamSingleDecl FormalParamSingleDecl) {
	}

	@Override
	public void visit(FormalParamsList FormalParamsList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void visit(NoFormPars NoFormPars) {
		hasArgs = false;
	}

	@Override
	public void visit(FormsPars FormsPars) {
		hasArgs = true;
	}

	@Override
	public void visit(MethTypeVoid MethTypeVoid) {
		currentMethodType = new Type("void");
		currentMethodType.struct = Tab.noType;
		// System.out.println(
		// "Povratna vrednost metode " + currentMethod.getName() + "je void na liniji "
		// + MethTypeVoid.getLine());
	}

	@Override
	public void visit(MethTypeNotVoid MethTypeNotVoid) {
		currentMethodType = MethTypeNotVoid.getType();
		currentType = null;
		System.out.println("Povratna vrednost metode je " + currentMethodType.getTypeName() + " na liniji "
				+ MethTypeNotVoid.getLine());
	}

	// TODO promeniti
	@Override
	public void visit(MethodDecl MethodDecl) {
		if (currentMethod == null) {
			System.out.println("current method je null");
			isCorrect = false;
			return;
		}
		if (!(currentMethodType.struct == Tab.noType) && !hasFoundReturn) {
			System.out.println("Nije pronadjen return za funkciju " + currentMethod.getName() + " povratni tip "
					+ currentMethodType.getTypeName());
			isCorrect = false;
		}

		if (currentMethod.getName().equals("main")) {
			if (currentMethodType.struct != Tab.noType) {
				System.out.println("Main metoda mora biti tipa void");
				isCorrect = false;
			} else if (hasArgs) {
				System.out.println("Main metoda ne sme da ima argumente");
				isCorrect = false;
			} else
				hasFoundMain = true;

		}
		Tab.chainLocalSymbols(currentMethod);
		Tab.closeScope();
		System.out.println("metoda ===============================");

		currentMethod = null;
		currentMethodType = null;
	}

	@Override
	public void visit(MethodName MethodName) {
		if (currentMethodType != null && currentMethodType.struct != Tab.nullType) {
			if (isFirstDeclaration(MethodName.getMethName(), MethodName.getLine())) {
				currentMethod = Tab.insert(Obj.Meth, MethodName.getMethName(), currentMethodType.struct);
				Tab.openScope();
				System.out.println("--------Deklarisana metoda " + MethodName.getMethName() + " povratna vrednost: "
						+ currentMethodType.getTypeName() + " na liniji " + MethodName.getLine());
			}
		} else {
			System.out.println("metoda Nulltype");
			isCorrect = false;
		}
	}

	@Override
	public void visit(Type Type) {
		Obj obj = Tab.find(Type.getTypeName());
		if (obj == Tab.noObj) {
			System.out.println("Tip nije pronadjen " + Type.getTypeName() + " na liniji " + Type.getLine());
			isCorrect = false;
			return;
		}
		if (obj.getKind() != Obj.Type) {
			System.out.println("Obj nije tip");
			isCorrect = false;
			return;
		}

		Type.struct = obj.getType();
		currentType = Type;
	}

	@Override
	public void visit(ClassName ClassName) {
		String className = ClassName.getClassName();
		int line = ClassName.getLine();
		if (isFirstDeclaration(className, line)) {
			currentClass = Tab.insert(Obj.Type, className, new Struct(Struct.Class));
			Tab.openScope();
			System.out.println("---------------------Deklarisana klasa " + className + " na liniji " + line);
			push(ScopeEnum.CLASS);
		} else {
			isCorrect = false;
		}
	}

	@Override
	public void visit(ClassDecl ClassDecl) {
		if (ScopeEnum.CLASS == peek()) {
			pop();
		} else {
			System.out.println("Greska classdecl scope tip");

			isCorrect = false;
			return;
		}

		currentClass = null;
	}

	@Override
	public void visit(VarAssigment VarAssigment) {
		if (currentType == null) {
			System.out.println("Tip nije dobar " + VarAssigment.getVarName() + " na liniji " + VarAssigment.getLine());
			isCorrect = false;
			return;
		}

		int kind = Obj.Var;
		if (currentClass != null) {
			kind = Obj.Fld;
			System.out.println("Polje klase");
		}

		Struct type = currentType.struct;
		if (isArray) {
			type = new Struct(Struct.Array, currentType.struct);
		}

		if (currentMethod != null) {
			System.out.println("Lokalna promenljiva - " + VarAssigment.getVarName() + " tipa "
					+ currentType.getTypeName() + (isArray ? "[] " : "") + " na liniji " + VarAssigment.getLine());
		} else {
			System.out.println("Globalna promenljiva - " + VarAssigment.getVarName() + " tipa "
					+ currentType.getTypeName() + (isArray ? "[] " : "") + " na liniji " + VarAssigment.getLine());
		}

		isArray = false;

		if (isFirstDeclaration(VarAssigment.getVarName(), VarAssigment.getLine())) {
			Tab.insert(kind, VarAssigment.getVarName(), type);
		}
	}

	@Override
	public void visit(VarDecl VarDecl) {
		currentType = null;
	}

	@Override
	public void visit(ArrayBracket ArrayBracket) {
		isArray = true;
	}

	@Override
	public void visit(NoArrayBracket NoArrayBracket) {
		isArray = false;
	}

	@Override
	public void visit(ConstAssigment ConstAssigment) {
		if (currentType == null) {
			System.out.println("Tip nije dobar");
			return;
		}

		if (isFirstDeclaration(ConstAssigment.getVarName(), ConstAssigment.getLine())) {
			ConstValue value = ConstAssigment.getConstValue();
			Struct type = null;
			if (value instanceof NumConst) {
				type = Tab.intType;
			}
			if (value instanceof CharConst) {
				type = Tab.charType;
			}
			if (value instanceof BoolConst) {
				type = boolType;
			}

			if (type == null) {
				System.out.println("Tip je null const assigment");
				isCorrect = false;
				return;
			}

			if (!type.assignableTo(currentType.struct)) {
				System.out.println("const tipovi se ne slazu");
				isCorrect = false;
				return;
			}

			System.out.println("Globalna konstanta " + ConstAssigment.getVarName() + " tipa "
					+ currentType.getTypeName() + " na liniji " + ConstAssigment.getLine());
			Tab.insert(Obj.Con, ConstAssigment.getVarName(), currentType.struct);
		}
	}

	@Override
	public void visit(ConstDecl ConstDecl) {
		currentType = null;
	}

	@Override
	public void visit(ProgClassDecl ProgClassDecl) {
		currentClass = null;
	}

	@Override
	public void visit(ProgVarDecl ProgVarDecl) {
		currentType = null;
	}

	@Override
	public void visit(ProgConstDecl ProgConstDecl) {
		currentType = null;
	}

	@Override
	public void visit(ProgName ProgName) {
		Tab.insert(Obj.Prog, ProgName.getProgName(), Tab.noType);
		System.out.println("------------------");
		Tab.openScope();
	}

	@Override
	public void visit(Program Program) {
		Obj program = Tab.find(Program.getProgName().getProgName());
		Tab.chainLocalSymbols(program);
		Tab.closeScope();
		System.out.println("program =================================");
	}

}
