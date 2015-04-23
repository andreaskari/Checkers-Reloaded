/* UserList.java */

import queue.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class UserList {
    private final static String BY_ID = "id";
    private final static String BY_PAGES_PRINTED = "pages";

    private CatenableQueue<User> userQueue;
    private int size;

    /**
    * Creates empty UserList containing no users
    **/
    public UserList(){
        userQueue = new CatenableQueue<User>();
        size = 0;
    }

    /**
    *  addUser() adds a defensive copy of the specified user.
    **/
    public void add(User u){
        if (u.getPagesPrinted() < 0) {
            System.out.println("A user cannot have a negative number of pages printed.");
            return;
        }
        User uCopy = new User(u.getId(), u.getPagesPrinted());
        userQueue.enqueue(uCopy);
        size++;
    }

    /**
    *  getSize() returns the number of users in the UserList
    **/
    public int getSize(){
        return size;
    }

    /**
    * getUsers() returns the CatenableQueue<User> of Users.
    **/
    public CatenableQueue<User> getUsers(){
        return userQueue;
    }

    /**
    *  toString() prints out the id and pages printed of all users in the UserList.
    **/
    public String toString(){
        return userQueue.toString();
    }

    /**
    *  partition() partitions qUnsorted using the pivot integer.  On completion of
    *  this method, qUnsorted is empty, and its items have been moved to qLess,
    *  qEqual, and qGreater, according to their relationship to the pivot.
    *
    *   @param sortFeature is a string that tells us what we are sorting. If we are
    *       sorting user IDs, sortFeatures equals "id". If we are sorting pages
    *       printed, sortFeatures equals "pages".
    *   @param qUnsorted is a CatenableQueue<User> of User objects.
    *   @param pivot is an integer used for partitioning.
    *   @param qLess is a CatenableQueue<User>, in which all Users with sortFeature less than pivot
    *       will be enqueued.
    *   @param qEqual is a CatenableQueue<User>, in which all Users with sortFeature equal to the pivot
    *       will be enqueued.
    *   @param qGreater is a CatenableQueue<User>, in which all Users with sortFeature greater than pivot
    *       will be enqueued.  
    **/ 
    public static void partition(String sortFeature, CatenableQueue<User> qUnsorted, int pivot, 
        CatenableQueue<User> qLess, CatenableQueue<User> qEqual, CatenableQueue<User> qGreater){

        while (qUnsorted.front() != null) {
            User front = qUnsorted.dequeue();
            int comparison = 0;
            if (sortFeature.equals(BY_ID)) {
                comparison = front.getId() - pivot;
            } else {
                comparison = front.getPagesPrinted() - pivot;
            }

            if (comparison < 0) {
                qLess.enqueue(front);
            } else if (comparison > 0) {
                qGreater.enqueue(front);
            } else {
                qEqual.enqueue(front);
            }
        }
    }

    /**
    *   quickSort() sorts q from smallest to largest according to sortFeature using quicksort.
    *   @param sortFeature is a string that tells us what we are sorting. If we are
    *       sorting user IDs, sortFeatures equals "id". If we are sorting pages
    *       printed, sortFeatures equals "pages".
    *   @param q is an unsorted CatenableQueue containing User items.
    **/
    public static void quickSort(String sortFeature, CatenableQueue<User> q){
        if (q.size() > 1) {
            CatenableQueue<User> qLess = new CatenableQueue<User>();
            CatenableQueue<User> qEqual = new CatenableQueue<User>();
            CatenableQueue<User> qGreater = new CatenableQueue<User>();

            int index = 0;
            if (sortFeature.equals(BY_ID)) {
                index = q.front().getId();
            } else {
                index = q.front().getPagesPrinted();
            }
            partition(sortFeature, q, index, qLess, qEqual, qGreater);

            quickSort(sortFeature, qLess);
            quickSort(sortFeature, qGreater);

            q.append(qLess);
            q.append(qEqual);
            q.append(qGreater);
        }
    }

    /**
    *  quickSort() sorts userQueue from smallest to largest according to sortFeature, using quicksort.
    *  @param sortFeature is a string that equals "id" if we are sorting users by their IDs, or equals
    *  "pages" if we are sorting users by the number of pages they have printed.
    **/
    public void quickSort(String sortFeature){
        quickSort(sortFeature, userQueue);
    }


    /**
    *  makeQueueOfQueues() makes a queue of queues, each containing one User
    *  of userQueue.  Upon completion of this method, userQueue is empty.
    *  @return a CatenableQueue<CatenableQueue<User>>, where each CatenableQueue
    *    contains one User from userQueue.
    **/
    public CatenableQueue<CatenableQueue<User>> makeQueueOfQueues(){
        CatenableQueue<CatenableQueue<User>> queueOfQueue = new CatenableQueue<CatenableQueue<User>>();
        while (userQueue.front() != null) {
            CatenableQueue<User> q = new CatenableQueue<User>();
            q.enqueue(userQueue.dequeue());
            queueOfQueue.enqueue(q);
        }
        return queueOfQueue;
    }

    /**
    *  mergeTwoQueues() merges two sorted queues into one sorted queue.  On completion
    *  of this method, q1 and q2 are empty, and their Users have been merged
    *  into the returned queue. Assume q1 and q2 contain only User objects.
    *   @param sortFeature is a string that tells us what we are sorting. If we are
    *       sorting user IDs, sortFeatures equals "id". If we are sorting pages
    *       printed, sortFeatures equals "pages".
    *  @param q1 is CatenableQueue<User> of User objects, sorted from smallest to largest by their sortFeature.
    *  @param q2 is CatenableQueue<User> of User objects, sorted from smallest to largest by their sortFeature.
    *  @return a CatenableQueue<User> containing all the Users from q1 and q2 (and nothing else),
    *       sorted from smallest to largest by their sortFeature.
    **/
    public static CatenableQueue<User> mergeTwoQueues(String sortFeature, CatenableQueue<User> q1, CatenableQueue<User> q2){
        CatenableQueue<User> merged = new CatenableQueue<User>();
        while (q1.size() > 0 && q2.size() > 0) {
            int comparison = 0;
            if (sortFeature.equals(BY_ID)) {
                comparison = q1.front().compareById(q2.front());
            } else {
                comparison = q1.front().compareByPagesPrinted(q2.front());
            }

            if (comparison <= 0) {
                merged.enqueue(q1.dequeue());
            } else {
                merged.enqueue(q2.dequeue());
            }
        }

        if (q1.size() > 0) {
            merged.append(q1);
        } else {
            merged.append(q2);
        }
        return merged;
    }

    /**
    *   mergeSort() sorts this UserList from smallest to largest according to sortFeature using mergesort.
    *   You should complete this method without writing any helper methods.
    *   @param sortFeature is a string that tells us what we are sorting. If we are
    *       sorting user IDs, sortFeatures equals "id". If we are sorting pages
    *       printed, sortFeatures equals "pages".
    **/
    public void mergeSort(String sortFeature){
        CatenableQueue<CatenableQueue<User>> queueOfQueue = makeQueueOfQueues();
        // CatenableQueue<CatenableQueue<User>> mergedQueueOfQueue = new CatenableQueue<CatenableQueue<User>>();
        // while (mergedQueueOfQueue.size() != 1) {
        //     while (queueOfQueue.size() > 1) {
        //         mergedQueueOfQueue.enqueue(mergeTwoQueues(sortFeature, queueOfQueue.dequeue(), queueOfQueue.dequeue()));
        //         if (queueOfQueue.size() == 1) {
        //             mergedQueueOfQueue.enqueue(queueOfQueue.dequeue());
        //         }
        //     }
        //     CatenableQueue<CatenableQueue<User>> temp = queueOfQueue;
        //     queueOfQueue = mergedQueueOfQueue;
        //     mergedQueueOfQueue = temp;
        // }
        // mergedQueueOfQueue = queueOfQueue.dequeue();
        while (queueOfQueue.size() != 1 && queueOfQueue.size() != 0) {
            queueOfQueue.enqueue(mergeTwoQueues(sortFeature, queueOfQueue.dequeue(), queueOfQueue.dequeue()));
        }
        userQueue = queueOfQueue.dequeue();
    }

    /**
    *   sortByBothFeatures() sorts this UserList's userQueue from smallest to largest pages printed.
    *   If two Users have printed the same number of pages, the User with the smaller user ID is first.
    **/
    public void sortByBothFeatures(){
        mergeSort(BY_ID);
        mergeSort(BY_PAGES_PRINTED);
    }


    @Test
    public void naivePartitionTest() {
        UserList list = new UserList();

        list.add(new User(0, 20));
        list.add(new User(1, 0));
        list.add(new User(2, 10));

        CatenableQueue<User> less = new CatenableQueue<User>();
        CatenableQueue<User> equal = new CatenableQueue<User>();
        CatenableQueue<User> greater = new CatenableQueue<User>();

        /* pivot on user 1 by id */
        list.partition("id", list.userQueue, 1, less, equal, greater);
        assertEquals(1, less.size());
        assertEquals(1, equal.size());
        assertEquals(1, greater.size());
        assertEquals(new User(0, 20), less.front());
        assertEquals(new User(1, 0), equal.front());
        assertEquals(new User(2, 10), greater.front());
    }

    @Test
    public void naiveQuickSortTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 11));

        list.quickSort("id");

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12 ]";

        assertEquals(sorted, list.toString());

        list.quickSort("pages");
        assertEquals(sorted, list.toString()); 
    }

    @Test
    public void goodQuickSortTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 11));
        list.add(new User(5, 1));
        list.add(new User(7, 4));
        list.add(new User(9, 8));
        list.add(new User(11, 7));
        list.add(new User(57, 3));

        list.quickSort("id");

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  " 
         + "User ID: 2, Pages Printed: 12,\n  User ID: 5, Pages Printed: 1,\n  "
         + "User ID: 7, Pages Printed: 4,\n  User ID: 9, Pages Printed: 8,\n  "
         + "User ID: 11, Pages Printed: 7,\n  User ID: 57, Pages Printed: 3 ]";

        assertEquals(sorted, list.toString());

        sorted =
         "[ User ID: 5, Pages Printed: 1,\n  User ID: 57, Pages Printed: 3,\n  " 
         + "User ID: 7, Pages Printed: 4,\n  User ID: 11, Pages Printed: 7,\n  "
         + "User ID: 9, Pages Printed: 8,\n  User ID: 0, Pages Printed: 10,\n  "
         + "User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12 ]";

        list.quickSort("pages");
        assertEquals(sorted, list.toString());
    }

    @Test
    public void naiveMakeQueuesTest(){
        UserList list = new UserList();

        list.add(new User(0, 20));
        list.add(new User(1, 0));
        list.add(new User(2, 10));

        CatenableQueue<CatenableQueue<User>> queues = list.makeQueueOfQueues();
        String queueOfQueues = 
        "[ [ User ID: 0, Pages Printed: 20 ],\n  [ User ID: 1, Pages Printed: 0 ],\n  [ User ID: 2, Pages Printed: 10 ] ]";

        assertEquals(queueOfQueues, queues.toString());        
    }

    @Test
    public void naiveMergeQueuesTest(){
        CatenableQueue<User> q1 = new CatenableQueue<User>();
        CatenableQueue<User> q2 = new CatenableQueue<User>();
        q1.enqueue(new User(0, 20));
        q2.enqueue(new User(1, 10));

        CatenableQueue<User> merged = mergeTwoQueues("pages", q1, q2);
        String mergeByPages = 
        "[ User ID: 1, Pages Printed: 10,\n  User ID: 0, Pages Printed: 20 ]";
        assertEquals(mergeByPages, merged.toString());        

        q1 = new CatenableQueue<User>();
        q2 = new CatenableQueue<User>();
        q1.enqueue(new User(0, 20));
        q2.enqueue(new User(1, 10));

        merged = mergeTwoQueues("id", q1, q2);
        String mergeById = 
        "[ User ID: 0, Pages Printed: 20,\n  User ID: 1, Pages Printed: 10 ]";
        assertEquals(mergeById, merged.toString());       
        assertEquals(0, q1.size());
        assertEquals(0, q2.size()); 
    }

    @Test
    public void goodMergeQueuesTest(){
        CatenableQueue<User> q1 = new CatenableQueue<User>();
        CatenableQueue<User> q2 = new CatenableQueue<User>();
        q1.enqueue(new User(0, 20));
        q2.enqueue(new User(3, 10));
        q1.enqueue(new User(2, 21));
        q2.enqueue(new User(5, 11));
        q2.enqueue(new User(5, 11));


        CatenableQueue<User> merged = mergeTwoQueues("pages", q1, q2);
        String mergeByPages = 
        "[ User ID: 3, Pages Printed: 10,\n  User ID: 5, Pages Printed: 11,\n  "
        + "User ID: 5, Pages Printed: 11,\n  User ID: 0, Pages Printed: 20,\n  "
        + "User ID: 2, Pages Printed: 21 ]";
        assertEquals(mergeByPages, merged.toString());        
        assertEquals(0, q1.size());
        assertEquals(0, q2.size());

        q1 = new CatenableQueue<User>();
        q2 = new CatenableQueue<User>();
        q1.enqueue(new User(0, 20));
        q2.enqueue(new User(3, 10));
        q1.enqueue(new User(2, 21));
        q2.enqueue(new User(5, 11));
        q2.enqueue(new User(5, 11));


        merged = mergeTwoQueues("id", q1, q2);
        String mergeById = 
        "[ User ID: 0, Pages Printed: 20,\n  User ID: 2, Pages Printed: 21,\n  "
        + "User ID: 3, Pages Printed: 10,\n  User ID: 5, Pages Printed: 11,\n  "
        + "User ID: 5, Pages Printed: 11 ]";
        assertEquals(mergeById, merged.toString());      
        assertEquals(0, q1.size());
        assertEquals(0, q2.size());
    }

    @Test
    public void naiveMergeSortTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 11));

        list.mergeSort("id");

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12 ]";

        assertEquals(sorted, list.toString());

        list.mergeSort("pages");
        assertEquals(sorted, list.toString()); 
    }

    @Test
    public void goodMergeSortTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 11));
        list.add(new User(5, 1));
        list.add(new User(7, 4));
        list.add(new User(9, 8));
        list.add(new User(11, 7));
        list.add(new User(57, 3));

        list.mergeSort("id");

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 11,\n  " 
         + "User ID: 2, Pages Printed: 12,\n  User ID: 5, Pages Printed: 1,\n  "
         + "User ID: 7, Pages Printed: 4,\n  User ID: 9, Pages Printed: 8,\n  "
         + "User ID: 11, Pages Printed: 7,\n  User ID: 57, Pages Printed: 3 ]";

        assertEquals(sorted, list.toString());

        sorted =
         "[ User ID: 5, Pages Printed: 1,\n  User ID: 57, Pages Printed: 3,\n  " 
         + "User ID: 7, Pages Printed: 4,\n  User ID: 11, Pages Printed: 7,\n  "
         + "User ID: 9, Pages Printed: 8,\n  User ID: 0, Pages Printed: 10,\n  "
         + "User ID: 1, Pages Printed: 11,\n  User ID: 2, Pages Printed: 12 ]";

        list.mergeSort("pages");
        assertEquals(sorted, list.toString());
    }

    @Test
    public void naiveSortByBothTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(1, 10));
        list.add(new User(0, 10));

        list.sortByBothFeatures();

        String sorted =
         "[ User ID: 0, Pages Printed: 10,\n  User ID: 1, Pages Printed: 10,\n  User ID: 2, Pages Printed: 12 ]";

        assertEquals(sorted, list.toString());
    }

    @Test
    public void goodSortByBothTest() {
        UserList list = new UserList();
        list.add(new User(2, 12));
        list.add(new User(0, 10));
        list.add(new User(1, 12));
        list.add(new User(5, 1));
        list.add(new User(7, 4));
        list.add(new User(9, 4));
        list.add(new User(11, 7));
        list.add(new User(57, 3));

        list.sortByBothFeatures();

        String sorted =
         "[ User ID: 5, Pages Printed: 1,\n  User ID: 57, Pages Printed: 3,\n  " 
         + "User ID: 7, Pages Printed: 4,\n  User ID: 9, Pages Printed: 4,\n  "
         + "User ID: 11, Pages Printed: 7,\n  User ID: 0, Pages Printed: 10,\n  "
         + "User ID: 1, Pages Printed: 12,\n  User ID: 2, Pages Printed: 12 ]";

        assertEquals(sorted, list.toString());
    }

    public static void main(String [] args) {
        // Naive right-idea tests. Just because these tests pass does NOT mean
        // your code is bug-free!

        // Uncomment the following line when ready
        jh61b.junit.textui.runClasses(UserList.class);
    }

}