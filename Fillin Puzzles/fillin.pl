% You can use this code to get started with your fillin puzzle solver.

:- ensure_loaded(library(clpfd)).

main(PuzzleFile, WordlistFile, SolutionFile) :-
	read_file(PuzzleFile, Puzzle),
	read_file(WordlistFile, Wordlist),
	valid_puzzle(Puzzle),
	solve_puzzle(Puzzle, Wordlist, Solved),
	print_puzzle(SolutionFile, Solved).

read_file(Filename, Content) :-
	open(Filename, read, Stream),
	read_lines(Stream, Content),
	close(Stream).

read_lines(Stream, Content) :-
	read_line(Stream, Line, Last),
	(   Last = true
	->  (   Line = []
	    ->  Content = []
	    ;   Content = [Line]
	    )
	;  Content = [Line|Content1],
	    read_lines(Stream, Content1)
	).

read_line(Stream, Line, Last) :-
	get_char(Stream, Char),
	(   Char = end_of_file
	->  Line = [],
	    Last = true
	; Char = '\n'
	->  Line = [],
	    Last = false
	;   Line = [Char|Line1],
	    read_line(Stream, Line1, Last)
	).

print_puzzle(SolutionFile, Puzzle) :-
	open(SolutionFile, write, Stream),
	maplist(print_row(Stream), Puzzle),
	close(Stream).

print_row(Stream, Row) :-
	maplist(put_puzzle_char(Stream), Row),
	nl(Stream).

put_puzzle_char(Stream, Char) :-
	(   var(Char)
	->  put_char(Stream, '_')
	;   put_char(Stream, Char)
	).



valid_puzzle([]).
valid_puzzle([Row|Rows]) :-
	maplist(samelength(Row), Rows).


samelength([], []).
samelength([_|L1], [_|L2]) :-
	same_length(L1, L2).


minimum(Zs,Min,Pos) :-
   maplist(#<(Min),Zs),
   nth0(Pos,Zs,Min).




%solve_puzzle(Puzzle, _, Puzzle).
solve_puzzle(Puzzle,Wordlist,Solved) :-
	replaceAll(Puzzle, Solved),
	transpose(Solved ,T2),
	append(Solved,T2,T),
	puzzleToSlot(T,Slots),

	recursion(Slots,Wordlist).

	
recursion([Slot],[Word]) :- Slot = Word.
recursion(Slots,Wordlist) :-
	slotsMatchWords(Slots,Wordlist,Res1, Res2),
	min_list(Res1,Num,Index),
	smallest(Num , Res1),  %最小的匹配数
	nth1(Index, Rest1, Num), %获得索引
	element_at(Slot,Slots,Index), %当前选的slot
	element_at(PosAns, Res2, Index),

	insert(PosAns,Slot),

	del(Slot,Slots,NewSlots),
	del(Slot,Wordlist,NextWordlist),
	recursion(NewSlots,NextWordlist).



inser(A,Slot) :- A = Slot.
insert([A1|Ast], Slot) :-
	( 
		A1 = Slot
	)
	;
	(
		insert(Ast,Slot)
	).




del(X,[X|Tail],Tail).
del(X,[Y|Tail],[Y|Tail1]):-
        del(X,Tail,Tail1).



head([Head], Head).
head([Head|Tail], Head).

element_at(X,[X|_],1).
element_at(X,[_|L],K) :- element_at(X,L,K1), K is K1 + 1.

indexof(Index, Item, List):-
  nth1(Index, List, Item).
indexof(-1, _, _).


min_list([X|Xs],Min,Index):-
    min_list(Xs,X,1,1,Min,Index).

min_list([],OldMin,OldIndex, _ , OldMin, OldIndex).
min_list([X|Xs],OldMin,_,CurrentIndex, Min, Index):-
    X < OldMin,
    NewCurrentIndex is CurrentIndex + 1,
    NewIndex is NewCurrentIndex,
    min_list(Xs, X, NewIndex, NewCurrentIndex, Min, Index).
min_list([X|Xs],OldMin,OldIndex,CurrentIndex, Min, Index):-
    X >= OldMin,
    NewCurrentIndex is CurrentIndex + 1,
    min_list(Xs, OldMin, OldIndex, NewCurrentIndex, Min, Index).



%smallest(Head, [Head]).
%smallest(Element, [Head|Tail]) :- smallest(E, Tail), Head =< E, Element is Head.
%smallest(Element, [Head|Tail]) :- smallest(E, Tail), E < Head , Element is E.



slotsMatchWords([S],Words,[R],[TR]) :- 
	matchWordsCollect(S, Words, TR),
	length(TR,R).
slotsMatchWords([S|Ss],Words,[R|Rs],[TR|TRs]) :-
	matchWordsCollect(S,Words,TR),
	length(TR,R),
	slotsMatchWords(Ss,Words,Rs,TRs).
	



matchWordsCollect(_,[],[]).
matchWordsCollect(Slot,[W|Ws],Result) :-
	( match(Slot, W) ->
		matchWordsCollect(Slot,Ws,R1),
		append([W],R1,Result)
	;
	  matchWordsCollect(Slot,Ws,Result)
	).


match([],[]).
match([S|Ss],[W|Ww]) :- 
	( var(S) ->
		match(Ss,Ww)
	;
		S == W,
	  	match(Ss,Ww)
	).


puzzleToSlot([],[]).
puzzleToSlot(T,S) :-
	split(T,S1),
	concatSlot(S1,S2),
	length(Lst,1),
	delete(S2,Lst,S3),
	delete(S3,[],S).
	



concatSlot([],[]).
concatSlot([A],A).
concatSlot([A,B|Rest], Result) :-
	append(A,B,R1),
	concatSlot(Rest, R2),
	append(R1,R2,Result).



split([],[]).
split([P|Ps],[S|Ss]) :-
    lineToSlots(P,[],[],S),
    split(Ps,Ss).



lineToSlots([], Re, Word, Res) :-
        reverse(Word, RealWord),
        Res = [RealWord|Re].


lineToSlots([H|Hs], Re, Word, Res) :-
    (
        H == '#'
    ->  reverse(Word, RealWord),
        lineToSlots(Hs, [RealWord|Re], [], Res)
    ;   lineToSlots(Hs, Re, [H|Word], Res)

    ).







replaceAll([],[]).
replaceAll([L1|R1],[L2|R2]) :-
	replace(L1,L2),
	replaceAll(R1,R2).


replace([],[]).
replace([E1|Rest1],[E2|Rest2]) :-
	( E1 == '_' ->
		E2 = _,
		replace(Rest1,Rest2)
	;
	  E2 = E1,
	  replace(Rest1,Rest2)
	).






% solve_puzzle(Puzzle0, WordList, Puzzle)
% should hold when Puzzle is a solved version of Puzzle0, with the
% empty slots filled in with words from WordList.  Puzzle0 and Puzzle
% should be lists of lists of characters (single-character atoms), one
% list per puzzle row.  WordList is also a list of lists of
% characters, one list per word.
%
% This code is obviously wrong: it just gives back the unfilled puzzle
% result.  You'll need to replace this with a working implementation.


