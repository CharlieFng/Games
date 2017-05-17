module Test (countCorrectCardsNum, getCardsRank , getCardsSuit, countCommonParts) where

import Card
import Data.List


countCorrectCardsNum :: Eq a => [a] -> [a] -> Int
countCorrectCardsNum target guess = sum([1 |each <- guess, elem each target ])


getCardsRank :: [Card] -> [Rank] 
getCardsRank cards = [rank card | card <- cards]

getCardsSuit :: [Card] -> [Suit]
getCardsSuit cards = [suit card | card <- cards]



countCommonParts :: Eq b => [b] -> [b] -> [b]
countCommonParts [] _ = []
countCommonParts _ [] = []
countCommonParts (x:xs) ys = if elem x ys 
	then x:countCommonParts xs (delete x ys)
	else countCommonParts xs ys




