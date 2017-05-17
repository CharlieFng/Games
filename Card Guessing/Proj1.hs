--Author : Feng Siyu
--Login Name : siyuf
--Student Id : 745399
-- This file include several functions, which define the procedure and algorithm of card guessing game.

-- To run this game, you should run a test file which already linked to this file followed by the cards 
-- of the answer on the command line. Then the program will pick five cards as a guess and calculate the
-- similarity of this guess and answer. According to the feedback (similarity), the program will make a 
-- next round guess recursively until the guess matches with answer. The main purpose is to guess the 
-- answer with fewest possible guesses.
module Proj1 (feedback, initialGuess, nextGuess, GameState(..)) where

import Card 
import Data.List


-- Takes a target and a guess, each represent as a list of cards, and returns five feedback numbers, 
-- which represents correct cards, lower ranks, correct ranks, higher ranks, correct suits respectively.
feedback target guess = 
    let targetCardsRank = getCardsRank target
        targetCardsSuit = getCardsSuit target
        guessCardsRank = getCardsRank guess
        guessCardsSuit = getCardsSuit guess
        a = countCorrectCardsNum target guess
        b = lowerRanksNum targetCardsRank guessCardsRank
        c = length (countCommonParts targetCardsRank guessCardsRank)
        d = higherRanksNum targetCardsRank guessCardsRank
        e = length (countCommonParts targetCardsSuit guessCardsSuit)
    in (a,b,c,d,e)
	

-- Calculate the number of cards in the answer are also in the guess.
countCorrectCardsNum :: Eq a => [a] -> [a] -> Int
countCorrectCardsNum target guess = sum([1 |each <- guess, elem each target ])

-- Calculate the number of cards in the answer have rank lower than the lowest rank in the guess.
lowerRanksNum :: [Rank] -> [Rank] -> Int
lowerRanksNum target guess = 
    let lowestRank = minimum guess
    in length (filter (<lowestRank) target)

-- Calculate the number of cards in the answer have rank higher than the highest rank in the guess.
higherRanksNum :: [Rank] -> [Rank] -> Int
higherRanksNum target guess = 
    let highestRank = maximum guess
    in length (filter (>highestRank) target)

-- Take a list of cards as input, return corresponding ranks in a list.
getCardsRank :: [Card] -> [Rank] 
getCardsRank cards = [rank card | card <- cards]

-- Take a list of cards as input, return corresponding suits in a list.
getCardsSuit :: [Card] -> [Suit]
getCardsSuit cards = [suit card | card <- cards]


-- Take two lists as input, return common elements in a list.
-- Duplicates can be counted, depends on the lower frequency in each list.
countCommonParts :: Eq b => [b] -> [b] -> [b]
countCommonParts [] _ = []
countCommonParts _ [] = []
countCommonParts (x:xs) ys = if elem x ys 
	then x:countCommonParts xs (delete x ys)
	else countCommonParts xs ys


--Self-defined data type, storing the cards of current guess and remaining possible answers.
--The next guess come from the remaining possible list.
data GameState = GameState { step::[Card], remainList::[[Card]] }
                deriving (Show,Eq)


--- Get all cards in order.
cardsCollection = [minBound..maxBound]::[Card]
-- Get all ranks in order.
ranksCollecton = [minBound..maxBound]::[Rank]
-- Get all suits in order.
suitsCollection = [minBound..maxBound]::[Suit]


-- Take the number of cards in the answer as input and return a pair of initial guess, which include 
-- a list of cards, and all possible cards combination generated from cardsCollection.
-- The algorithm for choosing initial cards is to choose different suits and with ranks about equally 
-- distant from each other and from the top and bottom ranks.
initialGuess :: Int -> ([Card], GameState)
initialGuess num = 
    (initialCards, GameState initialCards (combinations num cardsCollection) )
    where initialCards =  [ Card s r | (s,r) <- zip initialSuits initialRanks]
          initialSuits = take num suitsCollection
          initialRanks = everyNth (div 13 (num+1)) ranksCollecton 



-- Take a list and number N as input, return every Nth element in a list.
everyNth :: Int -> [t] -> [t]
everyNth n lst = case drop n lst of 
                                (y : ys) -> y : everyNth n ys
                                [] -> [] 


-- Generate all possible sublists of specific length without duplicate items from an orginal list. 
-- Implementation for combination formula.
combinations :: Int -> [a] -> [[a]]
combinations k xs = combinations' (length xs) k xs
  where combinations' n k' l@(y:ys)
          | k' == 0   = [[]]
          | k' >= n   = [l]
          | null l    = []
          | otherwise = map (y :) (combinations' (n - 1) (k' - 1) ys) ++ combinations' (n - 1) k' ys 


-- Take as input a pair of previous guess and game state, the feedback to this guess, return a pair of 
-- the next guess and new game state.
-- Pick a guess from last game state as next guess, then calculate the feedback between next guess 
-- with all other possible answers in last game state, collect all possible answers with the same feedback 
-- as that with answer into a list, as the next game state.
nextGuess :: ([Card],GameState) -> (Int,Int,Int,Int,Int) -> ([Card],GameState)
nextGuess (prev, prevState) hint 
      | crn == length prev = (prev, GameState prev [[]])
      | otherwise = (next, GameState next (filterPossibleAnswers prev possAns hint))
      where (crn,_,_,_,_) = hint
            possAns = remainList prevState
            filterPossibleAnswers guess possAns hint =
                 [item | item <- possAns, hint == feedback item guess ]
            next = pickAGuess (filterPossibleAnswers prev possAns hint)

-- Consider the tradeoff between speed and guess time, just pick the first one as next gess.
pickAGuess :: [t] -> t
pickAGuess [] = error "Can't get anything!"
pickAGuess (a:_) =  a 
