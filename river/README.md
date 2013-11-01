Todo:

backend

- use rtmpsclient instead of kassadin
- figure out if there is memory leak
- create more accounts

frontend

Features
    Frontend
        - put margin at bottom of clips table, put navigation bar w/ faq, contact, info
        - Dates:
            - make dates show X min ago, X days ago like lolking
                - fuzzy time, jquery/javascript, if it works, remove the java code I wrote to try and do this...
            - show date when you hover over it, make this date use timezone of client's browser

        - Steamer avatars
        - Streamer popup with info?
        - Watch Video needs to go to game page
        - clicking on random skin takes you to that random game
        - focus checkboxes when you press tab in search table

    Backend
        - support other game types like 3v3, aram, if people are interested
        - purge clips when videos get taken off twitch for some reason

Bugs
    - zooming in and out of browser (ctrl scroll or ctrl +) messes up page
    - reshaping browser causes search inputs like "Streamer Name" to wrap, causing search table to expand vertically
        - (putting white-space:no-wrap fixes problem, but introduces new one where table will wrap)
    - clip table cells should never change sizes, which happens when you keep clicking sort by


    Backend
        - Eliminate bad video conversion logs:
            - need to remove all videos with start_time < earliest start_time of that streamer
            - need to remove all games with players_info = null after I figure out where that bug is from
            - games that haven't had start_time and length set shouldn't be matched with a video

        - Eliminate bad game fetching:
            - players_info can be null, can have wrong size (gameId 1104957034)
