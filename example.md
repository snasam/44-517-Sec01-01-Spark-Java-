# Page rank algorithm explanation
PageRank or PR(A) can be calculated using a simple iterative algorithm, and corresponds to the principal eigenvector of the normalized link matrix of the web.
## Guess 1

We don’t know what their PR should be to begin with, so let’s take a guess at 1.0 and do some calculations:

d

= 0.85

PR(A)

= (1 – d) + d(PR(B)/1)

PR(B)

= (1 – d) + d(PR(A)/1)

i.e.

PR(A)

= 0.15 + 0.85 * 1
= 1

PR(B)

= 0.15 + 0.85 * 1
= 1

## Guess 2

No, that’s too easy, maybe I got it wrong (and it wouldn’t be the first time). Ok, let’s start the guess at 0 instead and re-calculate:

PR(A)

= 0.15 + 0.85 * 0
= 0.15


PR(B)

= 0.15 + 0.85 * 0.15
= 0.2775

NB. we’ve already calculated a “next best guess” at PR(A) so we use it here

And again:

PR(A)

= 0.15 + 0.85 * 0.2775
= 0.385875

PR(B)

= 0.15 + 0.85 * 0.385875
= 0.47799375

And again

PR(A)

= 0.15 + 0.85 * 0.47799375
= 0.5562946875

PR(B)

= 0.15 + 0.85 * 0.5562946875
= 0.622850484375

## Guess 3

Well let’s see. Let’s start the guess at 40 each and do a few cycles:

PR(A) = 40
PR(B) = 40

First calculation

PR(A)

= 0.15 + 0.85 * 40
= 34.25

PR(B)

= 0.15 + 0.85 * 0.385875
= 29.1775

And again

PR(A)

= 0.15 + 0.85 * 29.1775
= 24.950875

PR(B)

= 0.15 + 0.85 * 24.950875
= 21.35824375

Yup, those numbers are heading down alright! It sure looks the numbers will get to 1.0 and stop
