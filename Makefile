.PHONY: help start reset-db

help: ## Show this help
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-15s\033[0m %s\n", $$1, $$2}'

start: ## Start dev server
	./gradlew quarkusDev

reset-db: ## Reset database (drop volume + recreate)
	docker compose down -v postgres
	docker compose up -d postgres
