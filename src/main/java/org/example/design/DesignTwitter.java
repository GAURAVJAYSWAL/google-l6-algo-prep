package org.example.design;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * LC 355. Design Twitter.
 */
public class DesignTwitter {

    /**
     * Key insight: a global, ever-increasing timestamp stamped on every tweet makes
     * "most recent" a total order across all users. Each user keeps their own tweet
     * list (newest last) and a followee set. A news feed is then a k-way merge of the
     * relevant users' tweet lists: seed a max-heap with each list's newest tweet and
     * pop 10 times, pushing the predecessor after each pop. We never scan whole
     * histories — only as far back as the 10 freshest tweets require.
     *
     * Time:  postTweet O(1); follow/unfollow O(1); getNewsFeed O(F + 10 log F) for F
     *        followed users (self included) — heap seeded once, then 10 pops.
     * Space: O(T + F) — all tweets plus follow relationships.
     */
    static class Twitter {

        private static class Tweet {
            final int id;
            final int time;
            Tweet(int id, int time) { this.id = id; this.time = time; }
        }

        private int clock = 0;                                   // global tweet ordering
        private final Map<Integer, List<Tweet>> tweets = new HashMap<>();
        private final Map<Integer, Set<Integer>> following = new HashMap<>();

        public Twitter() {
        }

        public void postTweet(int userId, int tweetId) {
            tweets.computeIfAbsent(userId, k -> new ArrayList<>()).add(new Tweet(tweetId, clock++));
        }

        public List<Integer> getNewsFeed(int userId) {
            // Max-heap on tweet time; each heap entry remembers where it sits in its
            // owner's list so we can step backwards to the next-older tweet.
            PriorityQueue<int[]> heap = new PriorityQueue<>((a, b) -> b[0] - a[0]); // [time, owner, index]

            Set<Integer> sources = new HashSet<>(following.getOrDefault(userId, Set.of()));
            sources.add(userId);                                 // a user sees their own tweets
            for (int src : sources) {
                List<Tweet> list = tweets.get(src);
                if (list != null && !list.isEmpty()) {
                    int last = list.size() - 1;                  // newest tweet for this source
                    Tweet t = list.get(last);
                    heap.offer(new int[]{t.time, src, last});
                }
            }

            List<Integer> feed = new ArrayList<>(10);
            while (!heap.isEmpty() && feed.size() < 10) {
                int[] top = heap.poll();
                List<Tweet> list = tweets.get(top[1]);
                feed.add(list.get(top[2]).id);
                if (top[2] > 0) {                                // push the next-older tweet from same source
                    Tweet prev = list.get(top[2] - 1);
                    heap.offer(new int[]{prev.time, top[1], top[2] - 1});
                }
            }
            return feed;
        }

        public void follow(int followerId, int followeeId) {
            if (followerId == followeeId) return;
            following.computeIfAbsent(followerId, k -> new HashSet<>()).add(followeeId);
        }

        public void unfollow(int followerId, int followeeId) {
            Set<Integer> set = following.get(followerId);
            if (set != null) set.remove(followeeId);
        }
    }

    public static void main(String[] args) {
        Twitter twitter = new Twitter();
        twitter.postTweet(1, 5);
        System.out.println(twitter.getNewsFeed(1));   // expected: [5]
        twitter.follow(1, 2);
        twitter.postTweet(2, 6);
        System.out.println(twitter.getNewsFeed(1));   // expected: [6, 5] (newest first)
        twitter.unfollow(1, 2);
        System.out.println(twitter.getNewsFeed(1));   // expected: [5] (2's tweet drops out)

        Twitter t2 = new Twitter();
        for (int i = 1; i <= 12; i++) t2.postTweet(1, i);
        System.out.println(t2.getNewsFeed(1));        // expected: [12, 11, 10, 9, 8, 7, 6, 5, 4, 3] (cap of 10)
    }
}
