# Blaze-Persistence Not your father's DTOs

Many people advise against using the DTO pattern, but often for the wrong reasons.
It is often argued that DTOs are just copies of entities and that they represent unnecessary boilerplate,
but that is only the case for very simple or degenerate synthetic use cases.
When data crosses system boundaries, having DTOs of some sort is unavoidable at some point.
In this talk I will show you how Blaze-Persistence Entity Views makes "DTOs great again".
Entity Views allow you to model your read and write concerns for specific use cases in a clear succinct manner.
The best part is, that this approach will improve performance, reduce the scope of possible errors
and also make your intentions clearer by vastly reducing the amount of code necessary.
Take a look for yourself and see how Blaze-Persistence Entity Views are changing the game.
Brace yourselves for "Not your father's DTOs"!