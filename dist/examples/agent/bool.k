require "val.k"
module BOOL-EXP-SYNTAX
  syntax Exp ::= Bool
endmodule

module BOOL-EXP 
  imports VAL
  syntax Val ::= Bool 
endmodule
