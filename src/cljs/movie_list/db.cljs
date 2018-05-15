(ns movie-list.db)

(def default-db
  {:dialog-open? false
   :list-name "Hyvejä leffoja"
   :list [{:actors "Robert Downey Jr., Chris Hemsworth, Mark Ruffalo, Chris Evans",
            :genre "Action, Adventure, Fantasy",
            :imdb-votes "219,195",
            :released "27 Apr 2018",
            :writer "Christopher Markus (screenplay by), Stephen McFeely (screenplay by), Stan Lee (based on the Marvel comics by), Jack Kirby (based on the comics by), Joe Simon (Captain America created by), Jack Kirby (Captain America created by), Steve Englehart (Star-Lord created by), Steve Gan (Star-Lord created by), Bill Mantlo (Rocket Raccoon created by), Keith Giffen (Rocket Raccoon created by), Jim Starlin (Thanos, Gamora and Drax created by), Stan Lee (Groot created by), Larry Lieber (Groot created by), Jack Kirby (Groot created by), Steve Englehart (Mantis created by), Don Heck (Mantis created by)",
            :ratings [{:source "Internet Movie Database", :value "9.0/10"}
                      {:source "Rotten Tomatoes", :value "84%"}
                      {:source "Metacritic", :value "68/100"}],
            :website "http://marvel.com/movies/movie/223/avengers_infinity_war",
            :rated "PG-13",
            :title "Avengers: Infinity War",
            :poster "https://ia.media-imdb.com/images/M/MV5BMjMxNjY2MDU1OV5BMl5BanBnXkFtZTgwNzY1MTUwNTM@._V1_SX300.jpg",
            :awards "N/A",
            :imdb-id "tt4154756",
            :imdb-rating "9.0",
            :box-office "N/A",
            :production "Walt Disney Pictures",
            :director "Anthony Russo, Joe Russo",
            :response "True",
            :dvd "N/A",
            :type "movie",
            :runtime "149 min",
            :year "2018",
            :metascore "68",
            :country "USA",
            :language "English",
            :plot "The Avengers and their allies must be willing to sacrifice all in an attempt to defeat the powerful Thanos before his blitz of devastation and ruin puts an end to the universe."}
           {:actors "Chris Pratt, Zoe Saldana, Dave Bautista, Vin Diesel",
            :genre "Action, Adventure, Sci-Fi",
            :imdb-votes "362,736",
            :released "05 May 2017",
            :writer "James Gunn, Dan Abnett (based on the Marvel comics by), Andy Lanning (based on the Marvel comics by), Steve Englehart (Star-lord created by), Steve Gan (Star-lord created by), Jim Starlin (Gamora and Drax created by), Stan Lee (Groot created by), Larry Lieber (Groot created by), Jack Kirby (Groot created by), Bill Mantlo (Rocket Raccoon created by), Keith Giffen (Rocket Raccoon created by), Steve Gerber (Howard the Duck created by), Val Mayerik (Howard the Duck created by)",
            :ratings [{:source "Internet Movie Database", :value "7.7/10"}
                      {:source "Rotten Tomatoes", :value "83%"}
                      {:source "Metacritic", :value "67/100"}],
            :website "https://marvel.com/guardians",
            :rated "PG-13",
            :title "Guardians of the Galaxy Vol. 2",
            :poster "https://ia.media-imdb.com/images/M/MV5BMTg2MzI1MTg3OF5BMl5BanBnXkFtZTgwNTU3NDA2MTI@._V1_SX300.jpg",
            :awards "Nominated for 1 Oscar. Another 12 wins & 42 nominations.",
            :imdb-id "tt3896198",
            :imdb-rating "7.7",
            :box-office "$389,804,217",
            :production "Walt Disney Pictures",
            :director "James Gunn",
            :response "True",
            :dvd "22 Aug 2017",
            :type "movie",
            :runtime "136 min",
            :year "2017",
            :metascore "67",
            :country "USA",
            :language "English",
            :plot "The Guardians must fight to keep their newfound family together as they unravel the mystery of Peter Quill's true parentage."}]})
