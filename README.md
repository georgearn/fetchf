
## Disclaimer

This project is intended for **educational and personal use only**. The developer does not condone or encourage copyright infringement. The software is licensed under the [MIT License](https://github.com/mornyeapp/Mornye-Python/blob/main/LICENSE).


No copyrighted content is hosted, stored, mirrored, or distributed by this repository. The core application does not bundle, ship, or default to any third-party extension, registry, or provider. Extensions are installed only if a user explicitly configures and requests them from a source of their own choosing; the maintainer has no control over, does not review, and assumes no responsibility for the content or behavior of third-party extensions or registries a user may choose to install.

You are solely responsible for:

1. Ensuring your use of this software, and any extension or registry you choose to install, complies with your local laws.
2. Reading and adhering to the Terms of Service of any platform or provider you access, directly or through an extension.
3. Any legal consequences resulting from the use or misuse of this tool.

This software is provided free of charge by the maintainer. If you paid a third party for access to it, you may have been misled or scammed.

The software is provided "as is", without warranty of any kind, express or implied. The author assumes no liability for any bans, damages, or legal issues arising from its use or misuse. Users assume all risk associated with its use.

If you are a copyright holder or an authorized representative and believe this repository infringes upon your rights, please contact the maintainer with sufficient detail (including relevant URLs and proof of ownership); the matter will be promptly investigated.


## Why Python (instead of the standalone apps)

The standalone apps is built for direct, immediate use: open it, paste a link, download. Python exists for a different case — **integrating** this logic into something else.

It makes sense to start here if:

- **You're building a bot** (Telegram, Discord) or a service that needs to handle requests from many users automatically, not a single manual download.
- **You need the async API** (`AsyncMornye`) inside an existing FastAPI/Quart/Sanic app, with shared connection pooling.
- **You want to orchestrate bulk downloads** via scripts — multiple playlists, full discographies, local library retagging — with custom logic (filenames via a Python function, post-download actions, saved profiles).
- **You need to run headless**, on a server, a NAS, or inside Docker, possibly as part of a larger pipeline.
- **You want full control over which extensions get loaded** and from which registry, instead of relying on a fixed set baked into an app.

If you just want a GUI for personal use, with no code involved, the [Mobile](https://github.com/mornyeapp/Mornye) app remain the simpler choice — this module is the building block those (and similar projects) can be built on top of.

---

## Features

- Native synchronous and asynchronous Python APIs
- Modular JavaScript and Python Extension system (bring-your-own registry — nothing bundled)
- Automatic fallback among the extensions *you* have installed
- Built-in GUI, as a native window or served locally in a browser (`--gui` / `--web`)
- Guided terminal UI (`--tui`) with catalogue search, staged and live queues, download history, network-library tools, and extension management
- Docker support
- Configuration Profiles
- MusicBrainz metadata enrichment
- Synchronized lyrics — embedded in the tag and, optionally, written out as `.lrc` files; word-by-word where the source has it, or line-synced if you prefer
- Optional Canvas sidecars — the short looping visual a track carries, saved next to it as a video file (off by default; no media server reads these on its own)
- Optional animated Apple Music album artwork — saved as `cover.webp` beside downloaded tracks and compatible with Navidrome (off by default)
- Optional MP3 320 kbps transcoding
- Watch mode: re-sync a playlist/album/artist/URL on an interval, indefinitely
- Optional shared-secret or per-account authentication for `--web`, plus a queued, per-user download history in multi-user mode
- Extension discovery directories, and optional Ed25519 signature verification on top of registry checksums
- Per-extension quality choices and settings, plus metadata fallback and field-by-field merging for Spotify, Apple Music, and Tidal links
- Local-library duplicate detection by acoustic fingerprint, independent of tags/ISRC
- Extension scaffolding + dry-run validation for developing your own
- Installable as a PWA in `--web` mode (Add to Home Screen / standalone window)
- **Follow artists** — check for new releases and fetch only what came out since
- **Library upgrade** — find files below a target quality (including fake Hi-Res) and re-fetch them
- **Outbound notifications** — webhook, Discord, Telegram or ntfy, per track or one summary per run
- **Versioned REST API** at `/api/v1`, with an OpenAPI document at `/docs`
- **CSV input** — hand it a playlist export (Exportify, Soundiiz, a bare list of links) and it downloads what the file lists, matching rows that carry no link and reporting the ones it cannot
- **Dashboard** — what you have actually downloaded, as numbers: top artists, genres, decades, providers, listening time, activity by weekday and streaks (`--stats`, or the Stats screen)
- **Persistent staged download queue** (JSON) and **download log/history** (SQLite): queued work survives a restart, and completed downloads remain recorded
- **Network destinations** — use OS-mounted folders, SMB2/SMB3, or WebDAV, with staged and verified remote delivery
- **Per-account quotas and an admin role** in `--web-multiuser`
- **Extension health panel** — success rate, latency and last error per provider
- **Wrong-recording guard** — a provider that answers with a karaoke take, a clean edit of an explicit track, or another artist's cover is rejected and the download falls through to the next one, instead of producing a file that carries the right tags and the wrong audio

---

## Installation

```bash
pip install mornye
```

---

## Documentation

The full guide lives in [`docs/guide/`](docs/guide/) — the README had grown
to 76 KB and 87 headings, which is past the point where GitHub or PyPI
renders it usefully. Every page below is the same text that used to be here.

| Page | What's in it |
| --- | --- |
| [Quick Start](docs/guide/quick-start.md) | Every way to run it — GUI, web, CLI, terminal UI, Python — and the URL types it accepts |
| [Terminal UI](docs/guide/tui.md) | `--tui`, the guided mode: every setting on one screen, and the download queue live |
| [Extensions](docs/guide/extensions.md) | Registries, installing providers, signature verification and trust tiers |
| [Downloading from a CSV](docs/guide/csv.md) | Feeding it a file of tracks instead of a link: supported exports, how rows without a link are matched, and the unmatched-row report |
| [Your library in numbers](docs/guide/dashboard.md) | The dashboard built from what you have actually downloaded — top artists, genres, decades, activity |
| [Configuration](docs/guide/configuration.md) | Every option: naming, quality, lyrics, enrichment, transcoding, watch mode, profiles |
| [Network Destinations](docs/guide/network-destinations.md) | Mounted paths, SMB and WebDAV profiles, safe transfers, CLI and Docker |
| [API Reference](docs/guide/api-reference.md) | `Mornye` and `AsyncMornye`, and the objects they return |
| [Migration Guide](docs/guide/migration.md) | Moving from the old distribution, CLI, environment, and local state |
| [Local Tagging](docs/guide/local-tagging.md) | Retagging an existing library, MusicBrainz enrichment, download validation |
| [Automation & Operations](docs/guide/automation.md) | JSON output, post-download hooks, notifications, M3U, library rescan, cache maintenance, running the web server |
| [Following Artists](docs/guide/subscriptions.md) | Subscriptions, new-release checks, scheduled playlist monitoring, and the library-upgrade pass |
| [REST API](docs/guide/rest-api.md) | The versioned `/api/v1` surface, quotas and the admin endpoints |
| [Docker](docs/guide/docker.md) | Headless and NAS setups |
| [CLI](docs/guide/cli.md) | The standalone executables |

Security policy and the reporting process: [SECURITY.md](SECURITY.md).
