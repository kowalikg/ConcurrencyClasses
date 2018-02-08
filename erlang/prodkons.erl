-module(prodkons).
-export([start/0, server/1, createBuffer/1, buffer/2, findEmptyIndex/1, findFullIndex/1, createConsumers/2, createProducers/2, producer/1, consumer/1, produce/2, consume/1]).

start() ->
	%%BufferID = spawn(prodkons, buffer, [0, 0]),
	BufferList = createBuffer(5),
	ServerID = spawn(prodkons, server ,[BufferList]),
	createConsumers(10, ServerID),
	createProducers(10, ServerID),
	timer:kill_after(100, ServerID).

createBuffer(Number) ->
	if
		Number == 0 -> [];
		Number > 0 ->  [spawn(prodkons, buffer, [0,0]) | createBuffer(Number-1)]
  	end.
  
buffer(State, Value) -> %% states: 0 - free, 1 - pending, 2 - full
	receive
		{Pid, tellEmpty} -> %% buffer is asking
			if
				State == 0 -> 
					Pid ! free,
					buffer(1, Value);
				State > 0 ->
					Pid ! nonfree,
					buffer(State, Value)
			end;
		{Pid, tellFull} ->
			if
				State == 2 -> 
					Pid ! full,
					buffer(1, Value);
				State < 2 ->
					Pid ! nonfull,
					buffer(State, Value)
			end;	
		{ProducerID, putting, Portion} -> %% producer or consumer 
			ProducerID ! {complete},
			buffer(2, Portion);
		{ConsumerID, taking} ->
			ConsumerID ! {complete, Value},
			buffer(0, 0)
				
	end.

server(BufferList) ->
	receive
		{ProducerID, production} -> 
			BufferID = findEmptyIndex(BufferList), 
			if
				BufferID == 0 -> ProducerID ! fail; %% if no free space
				BufferID > 0 -> %% if is then send id to producer
					ProducerID ! {putting, BufferID}
			end,
			server(BufferList);
			
		{ConsumerID, consumption} -> 
			BufferID = findFullIndex(BufferList),
			if
				BufferID == 0 -> ConsumerID ! fail;
				BufferID > 0 -> 
					ConsumerID ! {taking, BufferID}
			end,
			server(BufferList)
	end.

findEmptyIndex([]) -> 0;
findEmptyIndex([H|T]) ->
	H ! {self(), tellEmpty},
    receive
        free ->  H;
        nonfree -> findEmptyIndex(T)
    end.

findFullIndex([]) -> 0;
findFullIndex([H|T]) ->
	H ! {self(), tellFull},
    receive
        full -> H;
        nonfull -> findFullIndex(T)
    end.	
	
createConsumers(Number, ServerID)->
	if
		Number == 0 -> io:format("");
		Number > 0 -> 
			createConsumers(Number-1, ServerID),
			spawn(prodkons, consumer, [ServerID])
	end.

createProducers(Number, ServerID)->
	if
		Number == 0 -> io:format("");
		Number > 0 -> 
			createProducers(Number-1, ServerID),
			spawn(prodkons, producer, [ServerID])
	end.

producer(ServerID) ->
	Number = rand:uniform(20),
	produce(Number, ServerID).
	
consumer(ServerID) ->
	consume(ServerID).

produce(Number, ServerID) ->
	ServerID ! {self(), production},
	receive
		{putting, BufferID} -> %% if receive id then put portion
			BufferID ! {self(), putting, Number},
			receive
				{complete} ->
				io:format("Producer: ~p successfully put ~p to buffer ~p~n", [self(), Number, BufferID]),
				producer(ServerID)
			end;
		fail -> 
			io:format("Producer: ~p failed with putting ~p to buffer~n", [self(), Number]),
			produce(Number, ServerID)
	end.

consume(ServerID) ->
	ServerID ! {self(), consumption},
	receive
		{taking, BufferID} ->
			BufferID ! {self(), taking},
			receive
				{complete, Value} ->
				io:format("Consumer: ~p successfully took ~p from buffer ~p~n", [self(), Value, BufferID]),
				consumer(ServerID)
			end;		
		fail -> 
			io:format("Consumer: ~p failed with taking from buffer~n", [self()]),
			consume(ServerID)
	end.
	
