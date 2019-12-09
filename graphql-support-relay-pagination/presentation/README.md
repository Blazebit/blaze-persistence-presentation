# Blaze-Persistence GraphQL Support - High performance querying and Relay pagination

GraphQL is getting more popular every day! More and more UI developers demand a GraphQL interface
so that they can do their work without wasting time on communication with the backend team.
There are solutions that allow exposing the whole JPA model, but doing that is rarely advisable.
Using annotations to exclude certain attributes or registering a special data fetcher to fetch attributes
requires manual wiring and is neither comfortable nor does it work well with the declarative schema approach.

Aren't GraphQL types just DTOs which we have written before? In many ways that is true, but to improve performance,
it is vital to make use of the GraphQL selection list feature to avoid loading unnecessary data.
When it comes to pagination, the GraphQL Relay spec provides guidance, but defining all the required types
and implementing the cursor pagination manually seems like a daunting task that should be done by a library.

Fortunately, Blaze-Persistence with Entity Views already solves all the underlying problems and comes to rescue.
Thanks to the latest addition, the GraphQL integration, which also implements schema generation for the Relay spec,
it is now easier than ever to provide a high performance GraphQL interface with minimal and mostly declarative code!

Join the talk and see for yourself how the Blaze-Persistence GraphQL integration will change your life!