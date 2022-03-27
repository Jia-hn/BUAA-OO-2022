# Unit 1

## 设计

### 总览

- 总体架构

  ![总体架构](D:\note\OO\Unit 1\总体架构.png)

- UML类图

  ![UML类图](D:\note\OO\Unit 1\UML类图.png)

### 数据结构

- Expr

  表达式(Expr)由项(Term)及其系数(BigInteger)组成。

- Term

  项(Term)由基数(Base)及其指数(Integer)组成。

- Base

  无

- Var

  变量

- Num

  常数

- Func

  由函数名称(String)及其表达式(Expr)组成。

### 算法

1. 解析（parse）

   使用**递归下降**的方法将输入字符串**解析**为表达式树一。表达式树一中包含非必要的括号，不满足题目要求。

2. 化简（simplify）

   使用**递归下降**的方法将表达式树一**化简**为表达式树二。表达式树二中只包含必要的括号，满足题目要求，但长度性能较差。

3. 优化（optimize）

   使用**递归下降**的方法将表达式树二**优化**为表达式树三。表达式树三中只包含必要的括号，满足题目要求，且长度性能较优。

4. 输出（toString）

   使用**递归下降**的方法将表达式树三**输出**为输出字符串。

## 实现

### 总览

- 类

  ![总览](D:\note\OO\Unit 1\总览.png)

- 代码行数

  ![代码行数](D:\note\OO\Unit 1\代码行数.png)

### 数据结构

- Expr

  ```java
  private final HashMap<Term, BigInteger> terms;
  ```

- Term

  ```java
  private final HashMap<Base, Integer> bases;
  ```

- Base

  无

- Var

  无

- Num

  ```java
  private final BigInteger constant;
  ```

- Func

  ```java
  private final String funcName;
  private final Expr expr;
  ```

### 算法

1. 解析(parse)

   - Expr

     ```java
     public Expr parseExpr() {
         Expr expr = new Expr();
         boolean flag = true;
         while (flag || lexer.getCurToken().equals("+") || lexer.getCurToken().equals("-")) {
             int sign = 1;
             if (lexer.getCurToken().equals("+")) {
                 lexer.next();
             } else if (lexer.getCurToken().equals("-")) {
                 lexer.next();
                 sign *= -1;
             }
             if (lexer.getCurToken().equals("+")) {
                 lexer.next();
             } else if (lexer.getCurToken().equals("-")) {
                 lexer.next();
                 sign *= -1;
             }
             if (sign == 1) {
                 expr.put(parseTerm(), BigInteger.ONE);
             } else {
                 expr.put(parseTerm(), BigInteger.ONE.negate());
             }
             flag = false;
         }
         return expr;
     }
     ```

   - Term

     ```java
     public Term parseTerm() {
         Term term = new Term();
         term.put(parseBase(), parseExponent());
         while (lexer.getCurToken().equals("*")) {
             lexer.next();
             term.put(parseBase(), parseExponent());
         }
         return term;
     }
     ```

   - Base

     ```java
     public Base parseBase() {
         Base base;
         if (lexer.getCurToken().equals("(")) {
             lexer.next();
             base = parseExpr();
             lexer.next();
         } else if (lexer.getCurToken().equals("sin") || lexer.getCurToken().equals("cos")) {
             base = parseFunc();
         } else if (lexer.getCurToken().equals("x")) {
             base = parseVar();
         } else if ("fgh".indexOf(lexer.getCurToken().charAt(0)) != -1) {
             base = parseCustomFunc();
         } else if (lexer.getCurToken().startsWith("sum")) {
             base = parseSumFunc();
         } else {
             base = parseNum();
         }
         return base;
     }
     ```

   - Var

     ```java
     public Var parseVar() {
         Var var = new Var();
         lexer.next();
         return var;
     }
     ```

   - Num

     ```java
     public Num parseNum() {
         Num num;
         int sign = 1;
         if (lexer.getCurToken().equals("-")) {
             sign *= -1;
         }
         if (lexer.getCurToken().equals("+") || lexer.getCurToken().equals("-")) {
             lexer.next();
         }
         if (sign == -1) {
             num = new Num(new BigInteger("-" + lexer.getCurToken()));
         } else {
             num = new Num(new BigInteger(lexer.getCurToken()));
         }
         lexer.next();
         return num;
     }
     ```

   - Func

     ```java
     public Func parseFunc() {
         Func func;
         String funcName = lexer.getCurToken();
         lexer.next();
         lexer.next();
         func = new Func(funcName, parseExpr());
         lexer.next();
         return func;
     }
     ```

2. 简化(simplify)

   - Expr

     ```java
     public Expr simplify() {
         Expr expr = new Expr();
         for (Term term : terms.keySet()) {
             Expr temp = new Expr();
             temp.put(new Term(), terms.get(term));
             expr = expr.add(term.simplify().multiply(temp));
         }
         return expr;
     }
     ```

   - Term

     ```java
     public Expr simplify() {
         Expr expr = Expr.one();
         for (Base base : bases.keySet()) {
             expr = expr.multiply(base.simplify().pow(get(base)));
         }
         return expr;
     }
     ```

   - Base

     ```java
     Expr simplify();
     ```

   - Var

     ```java
     public Expr simplify() {
         Expr expr = new Expr();
         Term term = new Term();
         term.put(new Var(), 1);
         expr.put(term, BigInteger.ONE);
         return expr;
     }
     ```

   - Num

     ```java
     public Expr simplify() {
         Expr expr = new Expr();
         expr.put(new Term(), constant);
         return expr;
     }
     ```

   - Func

     ```java
     public Expr simplify() {
         Expr expr = new Expr();
         Term term = new Term();
         term.put(new Func(funcName, this.expr.simplify()), 1);
         expr.put(term, BigInteger.ONE);
         return expr;
     }
     ```

3. 优化(optimize)

   - Expr

     ```java
     public Expr optimize1() {
         Expr expr = new Expr();
         for (Term term : terms.keySet()) {
             Expr temp = new Expr();
             temp.put(new Term(), terms.get(term));
             expr = expr.add(term.optimize1().multiply(temp));
         }
         return expr;
     }
     
     public Expr optimize2() {
         Expr expr = new Expr();
         for (Term term : terms.keySet()) {
             expr.put(term.optimize2(), get(term));
         }
         expr.merge1();
         return expr;
     }
     
     public Expr optimize3() {
         Expr expr = new Expr();
         for (Term term : terms.keySet()) {
             expr.put(term.optimize3(), get(term));
         }
         expr.merge2();
         return expr;
     }
     
     public Expr optimize() {
         Expr expr1 = this.deepCopy();
         Expr expr2 = this.deepCopy();
         boolean flag = true;
         while (flag || !String.valueOf(expr1).equals(String.valueOf(expr2))) {
             expr1 = expr2.deepCopy();
             expr2 = expr1.optimize1().optimize2().optimize3();
             flag = false;
         }
         return expr1;
     }
     ```

   - Term

     ```java
     public Expr optimize1() {
         Expr expr = Expr.one();
         for (Base base : bases.keySet()) {
             expr = expr.multiply(base.optimize1().pow(get(base)));
         }
         return expr;
     }
     
     public Term optimize2() {
         Term term = new Term();
         for (Base base : bases.keySet()) {
             term.put(base.optimize2(), get(base));
         }
         return term;
     }
     
     public Term optimize3() {
         Term term = new Term();
         for (Base base : bases.keySet()) {
             term.put(base.optimize3(), get(base));
         }
         return term;
     }
     ```

   - Base

     ```java
     Expr optimize1();
     
     Base optimize2();
     
     Base optimize3();
     ```

   - Var

     ```java
     public Expr optimize1() {
         Expr expr = new Expr();
         Term term = new Term();
         term.put(new Var(), 1);
         expr.put(term, BigInteger.ONE);
         return expr;
     }
     
     public Var optimize2() {
         return new Var();
     }
     
     public Var optimize3() {
         return new Var();
     }
     ```

   - Num

     ```java
     public Expr optimize1() {
         return null;
     }
     
     public Num optimize2() {
         return null;
     }
     
     public Num optimize3() {
         return null;
     }
     ```

   - Func

     ```java
     public Expr optimize1() {
         Expr expr1 = new Expr();
         Term term = new Term();
         Expr expr2 = expr.optimize1();
         if (expr2.getTerms().size() == 0) {
             if (funcName.equals("sin")) {
                 expr1.put(term, BigInteger.ZERO);
             } else {
                 expr1.put(term, BigInteger.ONE);
             }
         } else if (expr2.get(expr2.getTerms().keySet().iterator().next()).
                 compareTo(BigInteger.ZERO) < 0) {
             expr2 = expr2.multiply(Expr.one().negate());
             if (funcName.equals("sin")) {
                 term.put(new Func(funcName, expr2), 1);
                 expr1.put(term, BigInteger.ONE.negate());
             } else {
                 term.put(new Func(funcName, expr2), 1);
                 expr1.put(term, BigInteger.ONE);
             }
         } else {
             term.put(new Func(funcName, expr2), 1);
             expr1.put(term, BigInteger.ONE);
         }
         return expr1;
     }
     
     public Func optimize2() {
         return new Func(funcName, expr.optimize2());
     }
     
     public Func optimize3() {
         return new Func(funcName, expr.optimize3());
     }
     ```

4. 输出(toString)

   - Expr

     ```java
     public String toString() {
         StringBuilder stringBuilder = new StringBuilder();
         Term temp = null;
         for (Term term : terms.keySet()) {
             if (get(term).compareTo(BigInteger.ZERO) > 0) {
                 stringBuilder.append("+");
                 stringBuilder.append(get(term));
                 if (term.getBases().size() != 0) {
                     stringBuilder.append("*");
                     stringBuilder.append(term);
                 }
                 temp = term;
                 break;
             }
         }
         for (Term term : terms.keySet()) {
             if (term != temp) {
                 stringBuilder.append("+");
                 stringBuilder.append(get(term));
                 if (term.getBases().size() != 0) {
                     stringBuilder.append("*");
                     stringBuilder.append(term);
                 }
             }
         }
         String ret = String.valueOf(stringBuilder);
         ret = ret.replaceAll("sin\\(x\\*x\\)", "sin(x**2)");
         ret = ret.replaceAll("cos\\(x\\*x\\)", "cos(x**2)");
         ret = ret.replaceAll("\\+1\\*", "+");
         ret = ret.replaceAll("-1\\*", "-");
         ret = ret.replaceAll("\\+-", "-");
         if (ret.equals("")) {
             ret = "0";
         }
         if (ret.charAt(0) == '+') {
             ret = ret.substring(1);
         }
         return ret;
     }
     ```

   - Term

     ```java
     public String toString() {
         StringBuilder stringBuilder = new StringBuilder();
         for (Base base : bases.keySet()) {
             if (stringBuilder.length() != 0) {
                 stringBuilder.append("*");
             }
             stringBuilder.append(base);
             if (base instanceof Var && get(base).equals(2)) {
                 stringBuilder.append("*x");
             } else if (!get(base).equals(1)) {
                 stringBuilder.append("**");
                 stringBuilder.append(get(base));
             }
         }
         return String.valueOf(stringBuilder);
     }
     ```

   - Base

     ```java
     String toString();
     ```

   - Var

     ```java
     public String toString() {
         return "x";
     }
     ```

   - Num

     ```java
     public String toString() {
         return String.valueOf(constant);
     }
     ```

   - Func

     ```java
     public String toString() {
         if (expr.getTerms().size() == 1) {
             Term term = expr.getTerms().keySet().iterator().next();
             if ((term.getBases().size() == 1 && expr.get(term).equals(BigInteger.ONE))
                     || term.getBases().size() == 0) {
                 return funcName + "(" + expr + ")";
             } else {
                 return funcName + "((" + expr + "))";
             }
         } else {
             return funcName + "((" + expr + "))";
         }
     }
     ```

## 例子

1. 输入字符串

   x\*sin((2\*x))\*2\*sin(x)\*cos(x)+(x+1)\*cos((2\*x))\*\*2-cos((2\*x))\*\*2

2. 表达式树一

   ![例子1](D:\note\OO\Unit 1\例子1.png)

3. 表达式树二

   ![例子2](D:\note\OO\Unit 1\例子2.png)

4. 表达式树三

   ![例子3](D:\note\OO\Unit 1\例子3.png)

5. 输出字符串

   x

## 心得体会

- 注重OOP思想

  由于继承、封装、多态的特性，使得代码更好扩展。同时，面向对象编程也符合人类对事物的认识。

- 重设计轻实现

  我们应把大量的时间花费在设计上，而不是急于实现。当我们设计完成后，我们就应该知道我们的代码可以完成哪些功能，而又不可以完成哪些功能。实现只是对设计的代码化，个人认为这是一个比较机械的过程。如果并没有完成设计就开始了代码实现，此时思路并不清晰，极易出现bug，而且当这一设计走不通时，代码还要重写。

- 化繁为简

  当我们面临一个复杂的工程时，可以将这一复杂的工程进行适当的简化，并在此基础上进行迭代开发，逐步解决复杂工程。

- 及时重构

  当我们需要对工程的功能进行扩展时，如果我们当前的架构已经不能实现该功能，或者我们为了实现这一功能导致架构很不优雅，这时我们就需要及时对我们的架构进行重构，设计出一个更好的架构。