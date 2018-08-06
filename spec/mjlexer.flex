package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{

	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}

%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

LineTerminator  = \r|\n|\r\n
WhiteSpace      = {LineTerminator} | [ \t\f\b]

%%

{WhiteSpace} { }


"program"   { return new_symbol(sym.PROG, yytext()); }
"const"  	{ return new_symbol(sym.CONST, yytext()); }
"break"   	{ return new_symbol(sym.BREAK, yytext()); }
"class"   	{ return new_symbol(sym.CLASS, yytext()); }
"else"   	{ return new_symbol(sym.ELSE, yytext()); }
"if"   		{ return new_symbol(sym.IF, yytext()); }
"new"  		{ return new_symbol(sym.NEW, yytext()); }
"print"   	{ return new_symbol(sym.PRINT, yytext()); }
"read"   	{ return new_symbol(sym.READ, yytext()); }
"return"   	{ return new_symbol(sym.RETURN, yytext()); }
"void"   	{ return new_symbol(sym.VOID, yytext()); }
"do"   		{ return new_symbol(sym.DO, yytext()); }
"while"   	{ return new_symbol(sym.WHILE, yytext()); }
"extends"  	{ return new_symbol(sym.EXTENDS, yytext()); }
"continue"  { return new_symbol(sym.CONTINUE, yytext()); }


"+"   		{ return new_symbol(sym.PLUS, yytext()); }
"-"   		{ return new_symbol(sym.MINUS, yytext()); }
"*"   		{ return new_symbol(sym.MUL, yytext()); }
"/"   		{ return new_symbol(sym.DIV, yytext()); }
"%"   		{ return new_symbol(sym.MOD, yytext()); }
"=="   		{ return new_symbol(sym.IS_EQUAL, yytext()); }
"!="   		{ return new_symbol(sym.NOT_EQUAL, yytext()); }
">"   		{ return new_symbol(sym.GREATER, yytext()); }
">="   		{ return new_symbol(sym.GREATER_OR_EQUAL, yytext()); }
"<"   		{ return new_symbol(sym.LESS, yytext()); }
"<="  		{ return new_symbol(sym.LESS_OR_EQUAL, yytext()); }
"&&"   		{ return new_symbol(sym.LOGICAL_AND, yytext()); }
"||"   		{ return new_symbol(sym.LOGICAL_OR, yytext()); }
"="   		{ return new_symbol(sym.EQUAL, yytext()); }
"++"   		{ return new_symbol(sym.INC, yytext()); }
"--"   		{ return new_symbol(sym.DEC, yytext()); }
";"   		{ return new_symbol(sym.SEMI, yytext()); }
","   		{ return new_symbol(sym.COMMA, yytext()); }
"."   		{ return new_symbol(sym.DOT, yytext()); }
"("   		{ return new_symbol(sym.LPAREN, yytext()); }
")"   		{ return new_symbol(sym.RPAREN, yytext()); }
"["   		{ return new_symbol(sym.LBRACKET, yytext()); }
"]"   		{ return new_symbol(sym.RBRACKET, yytext()); }
"{"   		{ return new_symbol(sym.LBRACE, yytext()); }
"}"   		{ return new_symbol(sym.RBRACE, yytext()); }


"//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

[0-9]+  				{ return new_symbol(sym.NUM_CONST, new Integer (yytext())); }
("true" | "false") 		{ if (yytext().equals("true")) return new_symbol(sym.BOOL_CONST, true); return new_symbol(sym.BOOL_CONST, false);}
\'([a-z]|[A-Z])\' 		{ return new_symbol(sym.CHAR_CONST, new Character(yytext().charAt(1)));}


([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{ System.out.println("ident: " + yytext());  return new_symbol (sym.IDENT, yytext()); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }






