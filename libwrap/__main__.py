from __future__ import annotations

import argparse
import json
import sys

from .api import find_duplicates, scan_library


def main(argv: list[str] | None = None) -> int:
    p = argparse.ArgumentParser(prog="libwrap")
    sub = p.add_subparsers(dest="cmd", required=True)
    for name in ("scan", "dupes"):
        s = sub.add_parser(name)
        s.add_argument("root")
        s.add_argument("--no-recursive", action="store_true")
    sub.choices["dupes"].add_argument(
        "--match", choices=["isrc", "tags", "both"], default="both"
    )
    args = p.parse_args(argv)
    rec = not args.no_recursive
    if args.cmd == "scan":
        data = scan_library(args.root, recursive=rec)
    else:
        data = find_duplicates(args.root, recursive=rec, match=args.match)
    json.dump(data, sys.stdout, indent=2, ensure_ascii=False)
    sys.stdout.write("\n")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
