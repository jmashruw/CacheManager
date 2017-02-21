Application is about allowing multiple readers to read nodes in linkedlist and one writer at a time to write to list. Two kinds of threads share access to a singly-linked list: readers and writers. Readers merely examine the list, hence they can execute concurrently with each other. Writers add new items to the front of the list; write must be mutually exclusive to preclude two writers from writing new items at about the same time. However, one write can proceed in parallel with any number of reades. No data races between concurrent writers and readers!

Tester class creates a threadpool to invoke readers and writers randomly. We will be using futures cachepool to do the job. Once a reader or writer has finished its job, it will leave the thread and cache pool will reuse this thread.

Node class provides basic structure of each element in a linkedlist.

Reader class provides functionality of reading elements less than or equal to the number requested.

Writer class provides functionality of writing new elemenet to linkedlist or maybe updating one if it already exist.

How to run:

go to command prompt:

locate the folder where tar.gz file is extracted.

type following in command prompt:
> make
> java Tester

