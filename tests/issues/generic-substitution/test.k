require "/modules/substitution.k"
module TEST
  imports SUBSTITUTION
  syntax Ints ::= List{Int, ","}
  syntax Exps ::= List{Exp, ","}
  syntax Exp ::= "let" Ints  "=" Exps "in" Exp [binder(1*->3), strict(2)]
             | "(" Exp ")" [bracket]
             | Int

  syntax KResult ::= Int
  
//  rule isBound('let_=_in_, 1) => "star"
//  rule isBounding('let_=_in_, 3) => true

  syntax List{K} ::= ExpList2KList(Exps) [function]
  rule ExpList2KList(.Exps) => .List{K}
  rule ExpList2KList(E:Exp, Es:Exps) => E,,ExpList2KList(Es)
 
  //rule isInt('#symInt(_)) => true

  syntax Variable ::= Int

  rule let Xs:Ints = Bs:Exps in E:Exp 
    => E[ExpList2KList(Bs)/ExpList2KList(Xs)]
    [transition]
  
  configuration
    <k>
      $PGM:K
    </k>
endmodule

