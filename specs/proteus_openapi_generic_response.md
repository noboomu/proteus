# Proteus OpenAPI Generic Page Response Contract

## Response Types

- `ServerResponse<T>` is not a response body schema.
- `PagedResponse<T>` response body properties:
  - `items`: `List<T>`.
  - `index`: integer.
  - `count`: integer.
  - `total`: integer.
  - `meta`: object.
- `PagedCollection<T>` response body properties:
  - `items`: `List<T>`.
  - `navigation`: `PageNavigation`.

## Java Signatures

- `ServerResponse<PagedResponse<Order>>`: OpenAPI response schema reference `PagedResponseOrder`.
- `CompletableFuture<ServerResponse<PagedResponse<Order>>>`: OpenAPI response schema reference `PagedResponseOrder`.
- `ServerResponse<PagedCollection<CustomerOrder>>`: OpenAPI response schema reference `PagedCollectionCustomerOrder`.

## OpenAPI Document

- Typed page component `items.items` schema reference: declared Java type argument.
- Existing response status code: retained.
- Existing response description: retained.
- Existing response media type: retained.
- Explicit raw `PagedResponse` response annotation: response schema reference for the declared generic method type.
- Explicit raw `PagedCollection` response annotation: response schema reference for the declared generic method type.
- Explicit response schema whose raw class differs from the Java response container: retained.
- Specialized page component: entry in `components.schemas`.
- Page item component: entry in `components.schemas`.

## Public Interface Verification

- `GET /v1/openapi.json`: HTTP 200 and OpenAPI JSON document.
- `ServerResponse<PagedResponse<Pojo>>` endpoint with an explicit raw `PagedResponse` annotation: HTTP 200 response schema reference `PagedResponsePojo`.
- `components.schemas.PagedResponsePojo.properties.items.items`: schema reference `Pojo`.
- SourceOne endpoint `CompletableFuture<ServerResponse<PagedResponse<Order>>>`: HTTP 200 response schema reference `PagedResponseOrder`.
- Frontend generation from the SourceOne OpenAPI document: operation response type `PagedResponseOrder`.
