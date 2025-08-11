-- =========================
-- 1) QUESTIONS TABLE
-- =========================
INSERT INTO question (id, question_title, difficulty_level, category) VALUES
                                                                          (1, 'Which data type is used to store true/false values in Java?', 'easy', 'java'),
                                                                          (2, 'Which access modifier allows visibility only within the same class in Java?', 'easy', 'java'),
                                                                          (3, 'Which collection in Java does not allow duplicate keys?', 'medium', 'java'),
                                                                          (4, 'Which method is used to compare the contents of two strings in Java?', 'medium', 'java'),
                                                                          (5, 'Which Java component includes the compiler (javac)?', 'hard', 'java'),

                                                                          (6, 'Which Python collection type is ordered and mutable?', 'easy', 'python'),
                                                                          (7, 'Which keyword is used to define a function in Python?', 'easy', 'python'),
                                                                          (8, 'Which built-in function returns the number of items in a list?', 'medium', 'python'),
                                                                          (9, 'Which operator checks if two variables point to the same object in Python?', 'medium', 'python'),
                                                                          (10, 'Which PEP describes the Zen of Python?', 'hard', 'python'),

                                                                          (11, 'Which keyword declares a block-scoped variable in JavaScript?', 'easy', 'javascript'),
                                                                          (12, 'Which operator checks for both value and type equality in JavaScript?', 'easy', 'javascript'),
                                                                          (13, 'What is returned when you divide 0 by 0 in JavaScript?', 'medium', 'javascript'),
                                                                          (14, 'Which array method adds one or more elements to the end of an array?', 'medium', 'javascript'),
                                                                          (15, 'A function with access to variables from its outer scope forms what concept in JavaScript?', 'hard', 'javascript'),

                                                                          (16, 'Which C++ data type is used to store whole numbers?', 'easy', 'cpp'),
                                                                          (17, 'Which directive is used to include a header file in C++?', 'easy', 'cpp'),
                                                                          (18, 'Which access specifier allows access only to derived classes in C++?', 'medium', 'cpp'),
                                                                          (19, 'Which operator is used to dynamically allocate memory in C++?', 'medium', 'cpp'),
                                                                          (20, 'What is a function with no implementation in a base class called in C++?', 'hard', 'cpp');

-- =========================
-- 2) QUESTION OPTIONS TABLE
-- =========================
INSERT INTO question_options (id, option, right_answer, question_id) VALUES
-- Q1
(1, 'int', false, 1),
(2, 'float', false, 1),
(3, 'boolean', true, 1),
(4, 'char', false, 1),

-- Q2
(5, 'public', false, 2),
(6, 'private', true, 2),
(7, 'protected', false, 2),
(8, 'default', false, 2),

-- Q3
(9, 'Stack', false, 3),
(10, 'Queue', false, 3),
(11, 'HashMap', true, 3),
(12, 'ArrayList', false, 3),

-- Q4
(13, '==', false, 4),
(14, 'equals()', true, 4),
(15, 'compare()', false, 4),
(16, 'match()', false, 4),

-- Q5
(17, 'JVM', false, 5),
(18, 'JRE', false, 5),
(19, 'JDK', true, 5),
(20, 'JavaFX', false, 5),

-- Q6
(21, 'list', true, 6),
(22, 'tuple', false, 6),
(23, 'set', false, 6),
(24, 'dict', false, 6),

-- Q7
(25, 'def', true, 7),
(26, 'function', false, 7),
(27, 'func', false, 7),
(28, 'lambda', false, 7),

-- Q8
(29, 'len()', true, 8),
(30, 'size()', false, 8),
(31, 'count()', false, 8),
(32, 'length()', false, 8),

-- Q9
(33, 'is', true, 9),
(34, '==', false, 9),
(35, 'equals', false, 9),
(36, '===', false, 9),

-- Q10
(37, 'PEP 8', false, 10),
(38, 'PEP 20', true, 10),
(39, 'PEP 404', false, 10),
(40, 'PEP 302', false, 10),

-- Q11
(41, 'var', false, 11),
(42, 'let', true, 11),
(43, 'const', false, 11),
(44, 'static', false, 11),

-- Q12
(45, '==', false, 12),
(46, '===', true, 12),
(47, '=', false, 12),
(48, '!=', false, 12),

-- Q13
(49, 'NaN', true, 13),
(50, 'undefined', false, 13),
(51, 'null', false, 13),
(52, 'false', false, 13),

-- Q14
(53, 'push()', true, 14),
(54, 'append()', false, 14),
(55, 'add()', false, 14),
(56, 'insert()', false, 14),

-- Q15
(57, 'closure', true, 15),
(58, 'callback', false, 15),
(59, 'promise', false, 15),
(60, 'async', false, 15),

-- Q16
(61, 'int', true, 16),
(62, 'float', false, 16),
(63, 'char', false, 16),
(64, 'string', false, 16),

-- Q17
(65, '#include', true, 17),
(66, '#define', false, 17),
(67, 'import', false, 17),
(68, 'using', false, 17),

-- Q18
(69, 'public', false, 18),
(70, 'private', false, 18),
(71, 'protected', true, 18),
(72, 'friend', false, 18),

-- Q19
(73, 'new', true, 19),
(74, 'malloc', false, 19),
(75, 'allocate', false, 19),
(76, 'create', false, 19),

-- Q20
(77, 'Virtual Function', false, 20),
(78, 'Inline Function', false, 20),
(79, 'Friend Function', false, 20),
(80, 'Pure Virtual Function', true, 20);
